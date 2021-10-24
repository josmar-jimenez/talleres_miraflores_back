package com.inventory_system.backend.controller;

import com.inventory_system.backend.dto.request.product.ProductRequestDTO;
import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.dto.response.product.ProductResponseDTO;
import com.inventory_system.backend.enums.Action;
import com.inventory_system.backend.model.Product;
import com.inventory_system.backend.service.ProductService;
import com.inventory_system.backend.service.RoleOperativeActionService;
import com.inventory_system.backend.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	TokenService tokenService;
	@Autowired
	RoleOperativeActionService roleOperativeActionService;
	@Autowired
	ProductService productService;
	@Autowired
	ModelMapper modelMapper;

	private final int OPERATIVE = 3;

	@GetMapping("/{id}")
	public StandardResponse getProduct(@PathVariable(value = "id")Integer id) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		Product product  = productService.findById(id);
		ProductResponseDTO productResponseDTO = modelMapper.map(product, ProductResponseDTO.class);
		return StandardResponse.createResponse(productResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));

	}

	@PostMapping
	public StandardResponse createProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.CREATE.ordinal());
		Product product  = productService.create(productRequestDTO);
		ProductResponseDTO productResponseDTO = modelMapper.map(product, ProductResponseDTO.class);
		return StandardResponse.createResponse(productResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@PutMapping("/{id}")
	public StandardResponse updateProduct(@RequestBody @Valid  ProductRequestDTO productRequestDTO,
									   @PathVariable(value = "id")Integer id) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.UPDATE.ordinal());
		Product product  = productService.update(productRequestDTO, id);
		ProductResponseDTO productResponseDTO = modelMapper.map(product, ProductResponseDTO.class);
		return StandardResponse.createResponse(productResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@GetMapping
	public StandardResponse getProducts(Pageable pageable) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		Page<ProductResponseDTO> page = productService.findAll(pageable).map(store ->
				modelMapper.map(store, ProductResponseDTO.class));
		return StandardResponse.createResponse(page,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@DeleteMapping("/{id}")
	public StandardResponse deleteProduct( @PathVariable(value = "id")Integer id) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.DELETE.ordinal());
		boolean response  = productService.delete(id);
		return StandardResponse.createResponse(response,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}
}