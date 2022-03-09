package com.inventory_system.backend.controller;

import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.service.SummaryService;
import com.inventory_system.backend.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/summary")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SummaryController {

    @Autowired
    TokenService tokenService;
    @Autowired
    SummaryService summaryService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public StandardResponse getSummarys() throws Exception {
        return StandardResponse.createResponse(summaryService.findAll(),
                tokenService.getJWTToken(tokenService.getUserNick()));
    }

}