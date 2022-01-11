package com.inventory_system.backend.controller;

import com.inventory_system.backend.dto.request.inventory.InventoryRequestDTO;
import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.dto.response.inventory.InventoryResponseDTO;
import com.inventory_system.backend.enums.Action;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.model.Inventory;
import com.inventory_system.backend.service.InventoryService;
import com.inventory_system.backend.service.RoleOperativeActionService;
import com.inventory_system.backend.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InventoryController {

	@Autowired
	TokenService tokenService;
	@Autowired
	RoleOperativeActionService roleOperativeActionService;
	@Autowired
	InventoryService inventoryService;
	@Autowired
	ModelMapper modelMapper;

	private final int OPERATIVE = 6;

	@GetMapping("/{id}")
	public StandardResponse getInventory(@PathVariable(value = "id")Integer id) throws Exception {
		Allowed allowed =roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		Inventory inventory  = inventoryService.findById(id, allowed);
		InventoryResponseDTO stockResponseDTO = modelMapper.map(inventory, InventoryResponseDTO.class);
		return StandardResponse.createResponse(stockResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));

	}

	@PostMapping
	public StandardResponse createInventory(@RequestBody @Valid InventoryRequestDTO inventoryRequestDTO) throws Exception {
		Allowed allowed= roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.CREATE.ordinal());
		Inventory inventory  = inventoryService.create(inventoryRequestDTO,allowed);
		InventoryResponseDTO stockResponseDTO = modelMapper.map(inventory, InventoryResponseDTO.class);
		return StandardResponse.createResponse(stockResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}


	@GetMapping
	public StandardResponse getInventories(Pageable pageable) throws Exception {
		Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		Page<InventoryResponseDTO> page = inventoryService.findAll(pageable, allowed).map(inventory ->
				modelMapper.map(inventory, InventoryResponseDTO.class));
		return StandardResponse.createResponse(page,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

}