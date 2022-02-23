package com.inventory_system.backend.controller;

import com.inventory_system.backend.dto.request.product.ProductFilterRequestDTO;
import com.inventory_system.backend.dto.request.stock.StockFilterRequestDTO;
import com.inventory_system.backend.dto.request.stock.StockRequestDTO;
import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.dto.response.product.ProductResponseDTO;
import com.inventory_system.backend.dto.response.stock.StockResponseDTO;
import com.inventory_system.backend.enums.Action;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.model.Stock;
import com.inventory_system.backend.service.RoleOperativeActionService;
import com.inventory_system.backend.service.StockService;
import com.inventory_system.backend.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/stock")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StockController {

    @Autowired
    TokenService tokenService;
    @Autowired
    RoleOperativeActionService roleOperativeActionService;
    @Autowired
    StockService stockService;
    @Autowired
    ModelMapper modelMapper;

    private final int OPERATIVE = 4;

    @GetMapping("/{id}")
    public StandardResponse getStock(@PathVariable(value = "id") Integer id) throws Exception {
        Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
        Stock stock = stockService.findById(id, allowed);
        StockResponseDTO stockResponseDTO = modelMapper.map(stock, StockResponseDTO.class);
        return StandardResponse.createResponse(stockResponseDTO,
                tokenService.getJWTToken(tokenService.getUserNick()));

    }

    @PostMapping
    public StandardResponse createStock(@RequestBody @Valid StockRequestDTO stockRequestDTO) throws Exception {
        Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.CREATE.ordinal());
        Stock stock = stockService.create(stockRequestDTO, allowed);
        StockResponseDTO stockResponseDTO = modelMapper.map(stock, StockResponseDTO.class);
        return StandardResponse.createResponse(stockResponseDTO,
                tokenService.getJWTToken(tokenService.getUserNick()));
    }

    @PutMapping("/{id}")
    public StandardResponse updateStock(@RequestBody @Valid StockRequestDTO stockRequestDTO,
                                        @PathVariable(value = "id") Integer id) throws Exception {
        Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.UPDATE.ordinal());
        Stock stock = stockService.update(stockRequestDTO, id, allowed);
        StockResponseDTO stockResponseDTO = modelMapper.map(stock, StockResponseDTO.class);
        return StandardResponse.createResponse(stockResponseDTO,
                tokenService.getJWTToken(tokenService.getUserNick()));
    }

    @GetMapping
    public StandardResponse getStocks(Pageable pageable) throws Exception {
        Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
        Page<StockResponseDTO> page = stockService.findAll(pageable, allowed).map(stock ->
                modelMapper.map(stock, StockResponseDTO.class));
        return StandardResponse.createResponse(page,
                tokenService.getJWTToken(tokenService.getUserNick()));
    }

    @PostMapping("/filtered")
    public StandardResponse getProductsFiltered(Pageable pageable, @RequestBody StockFilterRequestDTO stockFilterRequestDTO) throws Exception {
        roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
        Page<StockResponseDTO> page = stockService.findAllFiltered(pageable,stockFilterRequestDTO).map(stock ->
                modelMapper.map(stock, StockResponseDTO.class));
        return StandardResponse.createResponse(page,
                tokenService.getJWTToken(tokenService.getUserNick()));
    }

    @DeleteMapping("/{id}")
    public StandardResponse deleteStock(@PathVariable(value = "id") Integer id) throws Exception {
        Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.UPDATE.ordinal());
        boolean response = stockService.delete(id, allowed);
        return StandardResponse.createResponse(response,
                tokenService.getJWTToken(tokenService.getUserNick()));
    }

    //Carga masiva de stock fake
/*    @GetMapping("/stockFake")
    public StandardResponse createStockFake() throws Exception {
        boolean response = stockService.createFake();
        return StandardResponse.createResponse(response,
                tokenService.getJWTToken(tokenService.getUserNick()));
    }*/
}