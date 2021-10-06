package com.inventory_system.backend.controller;

import com.inventory_system.backend.dto.request.provider.ProviderRequestDTO;
import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.dto.response.provider.ProviderResponseDTO;
import com.inventory_system.backend.enums.Action;
import com.inventory_system.backend.model.Provider;
import com.inventory_system.backend.service.ProviderService;
import com.inventory_system.backend.service.RoleOperativeActionService;
import com.inventory_system.backend.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/provider")
public class ProviderController {

	@Autowired
	TokenService tokenService;
	@Autowired
	RoleOperativeActionService roleOperativeActionService;
	@Autowired
	ProviderService providerService;
	@Autowired
	ModelMapper modelMapper;

	private final int OPERATIVE = 5;

	@GetMapping("/{id}")
	public StandardResponse getProvider(@PathVariable(value = "id")Integer id) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		Provider provider  = providerService.findById(id);
		ProviderResponseDTO providerResponseDTO = modelMapper.map(provider, ProviderResponseDTO.class);
		return StandardResponse.createResponse(providerResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));

	}

	@PostMapping
	public StandardResponse createStore(@RequestBody @Valid ProviderRequestDTO providerRequestDTO) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.CREATE.ordinal());
		Provider provider  = providerService.create(providerRequestDTO);
		ProviderResponseDTO storeResponseDTO = modelMapper.map(provider, ProviderResponseDTO.class);
		return StandardResponse.createResponse(storeResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@PutMapping("/{id}")
	public StandardResponse updateUser(@RequestBody @Valid  ProviderRequestDTO providerRequestDTO,
									   @PathVariable(value = "id")Integer id) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.UPDATE.ordinal());
		Provider provider  = providerService.update(providerRequestDTO, id);
		ProviderResponseDTO storeResponseDTO = modelMapper.map(provider, ProviderResponseDTO.class);
		return StandardResponse.createResponse(storeResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@GetMapping
	public StandardResponse getStores(Pageable pageable) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		Page<ProviderResponseDTO> page = providerService.findAll(pageable).map(provider ->
				modelMapper.map(provider, ProviderResponseDTO.class));
		return StandardResponse.createResponse(page,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}
}