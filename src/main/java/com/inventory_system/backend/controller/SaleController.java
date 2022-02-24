package com.inventory_system.backend.controller;

import com.inventory_system.backend.dto.request.sale.SaleFilterRequestDTO;
import com.inventory_system.backend.dto.request.sale.SaleRequestDTO;
import com.inventory_system.backend.dto.request.stock.StockFilterRequestDTO;
import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.dto.response.sale.SaleResponseDTO;
import com.inventory_system.backend.dto.response.stock.StockResponseDTO;
import com.inventory_system.backend.enums.Action;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.model.Sale;
import com.inventory_system.backend.service.RoleOperativeActionService;
import com.inventory_system.backend.service.SaleService;
import com.inventory_system.backend.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/sale")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SaleController {

    @Autowired
    TokenService tokenService;
    @Autowired
    RoleOperativeActionService roleOperativeActionService;
    @Autowired
    SaleService saleService;
    @Autowired
    ModelMapper modelMapper;

    private final int OPERATIVE = 7;

    @GetMapping("/{id}")
    public StandardResponse getSale(@PathVariable(value = "id") Integer id) throws Exception {
        Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
        Sale sale = saleService.findById(id, allowed);
        SaleResponseDTO saleResponseDTO = modelMapper.map(sale, SaleResponseDTO.class);
        return StandardResponse.createResponse(saleResponseDTO,
                tokenService.getJWTToken(tokenService.getUserNick()));

    }

    @PostMapping
    public StandardResponse createSale(@RequestBody @Valid SaleRequestDTO saleRequestDTO) throws Exception {
        Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.CREATE.ordinal());
        Sale sale = saleService.create(saleRequestDTO, allowed);
        SaleResponseDTO storeResponseDTO = modelMapper.map(sale, SaleResponseDTO.class);
        return StandardResponse.createResponse(storeResponseDTO,
                tokenService.getJWTToken(tokenService.getUserNick()));
    }

    @GetMapping
    public StandardResponse getSales(Pageable pageable) throws Exception {
        Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
        Page<SaleResponseDTO> page = saleService.findAll(pageable, allowed).map(store ->
                modelMapper.map(store, SaleResponseDTO.class));
        return StandardResponse.createResponse(page,
                tokenService.getJWTToken(tokenService.getUserNick()));
    }

    @PostMapping("/filtered")
    public StandardResponse getSalesFiltered(Pageable pageable, @RequestBody SaleFilterRequestDTO saleFilterRequestDTO) throws Exception {
        roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
        Page<SaleResponseDTO> page = saleService.findAllFiltered(pageable,saleFilterRequestDTO).map(stock ->
                modelMapper.map(stock, SaleResponseDTO.class));
        return StandardResponse.createResponse(page,
                tokenService.getJWTToken(tokenService.getUserNick()));
    }
}