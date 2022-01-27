package com.inventory_system.backend.service;

import com.inventory_system.backend.config.ElasticSearchConnection;
import com.inventory_system.backend.dto.response.product.ProductResponseDTO;
import com.inventory_system.backend.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ElasticSearchService {

    @Autowired
    ElasticSearchConnection elasticSearchConnection;

    @Value("${elasticSearch.size}")
    String SUGGESTION_PAGE_SIZE;

    public void createIndex() {
        elasticSearchConnection.createIndex();
    }

    public void insertUpdateDocument(Product product) {
        elasticSearchConnection.insertDocument(product);
    }

    public List<ProductResponseDTO> getSuggestion(String input, List<String> tags) {
        return elasticSearchConnection.search(input, tags, SUGGESTION_PAGE_SIZE);
    }

    public void deleteDocument(Product product) {
        elasticSearchConnection.deleteDocument(product);
    }

}
