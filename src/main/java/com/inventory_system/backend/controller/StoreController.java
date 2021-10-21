package com.inventory_system.backend.controller;

import com.inventory_system.backend.dto.request.store.StoreRequestDTO;
import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.dto.response.store.StoreResponseDTO;
import com.inventory_system.backend.enums.Action;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.model.Store;
import com.inventory_system.backend.service.RoleOperativeActionService;
import com.inventory_system.backend.service.StoreService;
import com.inventory_system.backend.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/store")
public class StoreController {

	@Autowired
	TokenService tokenService;
	@Autowired
	RoleOperativeActionService roleOperativeActionService;
	@Autowired
	StoreService storeService;
	@Autowired
	ModelMapper modelMapper;

	private final int OPERATIVE = 2;

	@GetMapping("/{id}")
	public StandardResponse getStore(@PathVariable(value = "id")Integer id) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		Store store  = storeService.findById(id);
		StoreResponseDTO storeResponseDTO = modelMapper.map(store, StoreResponseDTO.class);
		return StandardResponse.createResponse(storeResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));

	}

	@PostMapping
	public StandardResponse createStore(@RequestBody @Valid StoreRequestDTO storeRequestDTO) throws Exception {
		Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.CREATE.ordinal());
		Store store  =storeService.create(storeRequestDTO,allowed);
		StoreResponseDTO storeResponseDTO = modelMapper.map(store, StoreResponseDTO.class);
		return StandardResponse.createResponse(storeResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@PutMapping("/{id}")
	public StandardResponse updateUser(@RequestBody @Valid  StoreRequestDTO storeRequestDTO,
									   @PathVariable(value = "id")Integer id) throws Exception {
		Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.UPDATE.ordinal());
		Store store  = storeService.update(storeRequestDTO, id,allowed);
		StoreResponseDTO storeResponseDTO = modelMapper.map(store, StoreResponseDTO.class);
		return StandardResponse.createResponse(storeResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@GetMapping
	public StandardResponse getStores(Pageable pageable) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		Page<StoreResponseDTO> page = storeService.findAll(pageable).map(store ->
				modelMapper.map(store, StoreResponseDTO.class));
		return StandardResponse.createResponse(page,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@DeleteMapping("/{id}")
	public StandardResponse deleteStore( @PathVariable(value = "id")Integer id) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.DELETE.ordinal());
		boolean response  = storeService.delete(id);
		return StandardResponse.createResponse(response,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}
}