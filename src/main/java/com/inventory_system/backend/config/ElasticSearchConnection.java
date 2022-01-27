package com.inventory_system.backend.config;

import com.inventory_system.backend.dto.response.product.ProductResponseDTO;
import com.inventory_system.backend.model.Product;
import com.inventory_system.backend.model.Stock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.completion.context.CategoryQueryContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Slf4j
@Component
public class ElasticSearchConnection {

    RestHighLevelClient restClient;

    @Value("${elasticSearch.ip}")
    String IP;
    @Value("${elasticSearch.port}")
    String PORT;
    @Value("${elasticSearch.protocol}")
    String PROTOCOL;
    @Value("${elasticSearch.index}")
    String INDEX_NAME;

    /*Create client rest elasticsearh*/
    public void createClient() {
        try {
            //restClient = new RestHighLevelClient(RestClient.builder(new HttpHost(IP, Integer.parseInt(PORT), PROTOCOL)));

            URI connUri = URI.create("https://p538yrnnd6:g7lguaua7u@apple-473953082.us-east-1.bonsaisearch.net:443");
            String[] auth = connUri.getUserInfo().split(":");
            CredentialsProvider cp = new BasicCredentialsProvider();
            cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(auth[0], auth[1]));

            restClient = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), connUri.getScheme()))
                            .setHttpClientConfigCallback(
                                    httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(cp)
                                            .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())));

            log.debug("Elastic-> Cliente elastic " + restClient);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /*Close explicity client*/
    public void closeClient() {
        try {
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Create index*/
    public boolean createIndex() {

        createClient();

        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);

        request.source(
                "{\n" +
                        "    \"settings\" : {\n" +
                        "        \"number_of_shards\" : 2,\n" +
                        "        \"number_of_replicas\" : 1,\n" +
                        "        \"analysis\": {\n" +
                        "            \"analyzer\": {\n" +
                        "               \"autocomplete\": {\n" +
                        "                   \"type\": \"custom\",\n" +
                        "                   \"tokenizer\": \"whitespace\"\n" +
                        "               }},\n" +
                        "               \"filter\": {\n" +
                        "                 \"autocomplete_filter\": {\n" +
                        "                   \"type\": \"edge_ngram\",\n" +
                        "                   \"min_gram\": 3,\n" +
                        "                   \"max_gram\": 20\n" +
                        "                 }\n" +
                        "               }\n" +
                        "           }\n" +
                        "       },\n" +
                        "  \"mappings\": {\n" +
                        "  \"properties\": {\n" +
                        "    \"product\": {\n" +
                        "      \"type\": \"completion\",\n" +
                        "       \"analyzer\": \"autocomplete\"," +
                        "       \"contexts\": [\n" +
                        "           {\n" +
                        "           \"name\":\"tags\",\n" +
                        "           \"type\":\"category\",\n" +
                        "           \"path\":\"cat\"}]\n" +
                        "   },\n" +
                        "    \"id\": {\n" +
                        "      \"type\": \"text\"\n" +
                        "    },\n" +
                        "    \"name\": {\n" +
                        "      \"type\": \"text\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "  }\n" +
                        "}"
                , XContentType.JSON
        );

        try {
            request.setMasterTimeout(TimeValue.MINUS_ONE);
            CreateIndexResponse createIndexResponse = restClient.indices().create(request, RequestOptions.DEFAULT);
            log.info("Created index correctly whith name " + createIndexResponse.index());
            closeClient();
            return true;
        } catch (Exception e) {
            log.error("Elastic-> Error creating index: " + e.getMessage());
            e.printStackTrace();
            closeClient();
            return false;
        }
    }

    /*Insert any documents*/
    public void insertDocument(Product product) {

        createClient();

        List<String> contextTags = new ArrayList<>();
        if (!CollectionUtils.isEmpty(product.getTag()))
            product.getTag().forEach(productTag -> contextTags.add("\"" + productTag.getTag().getName() + "\""));
        contextTags.add("\"all\"");

        String productNameCleaned = StringUtils.stripAccents(product.getName().toLowerCase());
        List<String> words = new ArrayList<>(Arrays.asList(productNameCleaned.trim().split(" ")));

        /* Remove connectors to optimize select */
        words.removeAll(Collections.singleton("la"));
        words.removeAll(Collections.singleton("el"));
        words.removeAll(Collections.singleton("del"));
        words.removeAll(Collections.singleton("al"));
        words.removeAll(Collections.singleton("de"));
        words.removeAll(Collections.singleton("las"));
        words.removeAll(Collections.singleton("a"));

        words.removeAll(Collections.singleton(","));
        words.removeAll(Collections.singleton("("));
        words.removeAll(Collections.singleton(")"));
        words.removeAll(Collections.singleton("."));

        String inputForElastic = "[";

        if (!words.isEmpty()) {

            if (words.size() == 1)
                inputForElastic += "{\"input\":\"" + words.get(0) + "\", \"weight\":5 , \"contexts\":{\"tags\":" + contextTags + "}}]";

            else {
                int i = words.size();

                while (words.size() > 1) {
                    inputForElastic += "{\"input\":\"" + String.join(" ", words) + "\", \"weight\":" + i + " ,\"contexts\":{\"tags\":" + contextTags + "}},";
                    words.remove(0);
                    i--;
                }
                inputForElastic += "{\"input\":\"" + words.get(0) + "\", \"weight\":" + i + ", \"contexts\":{\"tags\":" + contextTags + "}}]";
            }

            String json = "{" +
                    "\"product\":" + inputForElastic + "," +
                    "\"id\":\"" + product.getId() + "\"," +
                    "\"name\":\"" + product.getName() + "\"" +
                    "}";
            IndexRequest request = new IndexRequest(INDEX_NAME);
            request.id(product.getId().toString());
            request.source(json, XContentType.JSON);

            try {
                restClient.index(request, RequestOptions.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeClient();
        }
    }

    /*Autocomplete con suggest Search*/

    public List<ProductResponseDTO> search(String input, List<String> tags, String size) {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        CompletionSuggestionBuilder suggestBuilder = new CompletionSuggestionBuilder("product");

        /* Remove connectors to optimize select */
        String inputCleaned = StringUtils.stripAccents(input.toLowerCase());
        List<String> words = new ArrayList<>(Arrays.asList(inputCleaned.split(" ")));
        words.removeAll(Collections.singleton("la"));
        words.removeAll(Collections.singleton("el"));
        words.removeAll(Collections.singleton("del"));
        words.removeAll(Collections.singleton("al"));
        words.removeAll(Collections.singleton("de"));
        words.removeAll(Collections.singleton("las"));
        words.removeAll(Collections.singleton("a"));

        words.removeAll(Collections.singleton(","));
        words.removeAll(Collections.singleton("."));

        List<String> contextTags = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tags))
            contextTags = tags;
        else
            contextTags.add("all");

        suggestBuilder.size(Integer.valueOf(size))
                .text(String.join(" ", words))
                .prefix(String.join(" ", words), Fuzziness.ONE)
                .analyzer("autocomplete");

        List<CategoryQueryContext> contextTagList = new ArrayList();
        contextTags.forEach(s -> contextTagList.add(CategoryQueryContext.builder().setCategory(s).build()));
        suggestBuilder.contexts(Collections.singletonMap("tags", contextTagList));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.suggest(new SuggestBuilder().addSuggestion("products_autocomplete", suggestBuilder));
        searchRequest.source(sourceBuilder);
        SearchResponse response;
        try {
            createClient();
            response = restClient.search(searchRequest, RequestOptions.DEFAULT);
            closeClient();
            return getSuggestions(response);
        } catch (IOException ex) {
            log.error("Error in autocomplete search", ex);
            closeClient();
            return null;
        }
    }

    private List<ProductResponseDTO> getSuggestions(SearchResponse response) {

        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        Suggest suggestResponse = response.getSuggest();
        CompletionSuggestion suggestion = suggestResponse.getSuggestion("products_autocomplete");
        List<CompletionSuggestion.Entry> entries = suggestion.getEntries();

        for (CompletionSuggestion.Entry entry : entries) {
            for (CompletionSuggestion.Entry.Option option : entry.getOptions()) {
                ProductResponseDTO productFoundResponseDTO = new ProductResponseDTO();
                productFoundResponseDTO.setId(Integer.valueOf((String) option.getHit().getSourceAsMap().get("id")));
                productFoundResponseDTO.setName((String) option.getHit().getSourceAsMap().get("name"));
                productResponseDTOList.add(productFoundResponseDTO);
            }
        }
        return productResponseDTOList;
    }

    public void deleteDocument(Product product) {
        createClient();
        try {
            restClient.delete(new DeleteRequest(INDEX_NAME, product.getId().toString()), RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeClient();
    }

}