package com.inventory_system.backend.controller;

import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.service.ElasticSearchService;
import com.inventory_system.backend.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/elasticSearch")
public class AutocompleteController {

    @Autowired
    ElasticSearchService startup;
    @Autowired
    TokenService tokenService;


    @GetMapping("/runStartup")
    public ResponseEntity<String> runStartup() {
        log.debug("---------> Consuming method run_startup");

        StringBuilder stringResp = new StringBuilder();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_HTML);

        try {
            startup.createIndex();
            stringResp.append("<html>");
            stringResp.append("Index created</br>");
            stringResp.append("</html>");
            return new ResponseEntity<>(stringResp.toString(), responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("An error has occurred: " + e.getMessage());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getSuggestion")
    public StandardResponse getSuggestion(
            @RequestParam(name = "input") String input, @RequestParam(name = "tags", required = false) List<String> tags) {
        return StandardResponse.createResponse(startup.getSuggestion(input, tags), null);
    }
}
