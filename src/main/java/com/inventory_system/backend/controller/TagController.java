package com.inventory_system.backend.controller;

import com.inventory_system.backend.dto.request.tag.TagRequestDTO;
import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.dto.response.tag.TagResponseDTO;
import com.inventory_system.backend.enums.Action;
import com.inventory_system.backend.model.Tag;
import com.inventory_system.backend.service.RoleOperativeActionService;
import com.inventory_system.backend.service.TagService;
import com.inventory_system.backend.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tag")
public class TagController {

	@Autowired
	TokenService tokenService;
	@Autowired
	RoleOperativeActionService roleOperativeActionService;
	@Autowired
	TagService tagService;
	@Autowired
	ModelMapper modelMapper;

	private final int OPERATIVE = 8;

	@GetMapping("/{id}")
	public StandardResponse getTag(@PathVariable(value = "id")Integer id) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		Tag tag  = tagService.findById(id);
		TagResponseDTO tagResponseDTO = modelMapper.map(tag, TagResponseDTO.class);
		return StandardResponse.createResponse(tagResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));

	}

	@PostMapping
	public StandardResponse createTag(@RequestBody @Valid TagRequestDTO tagRequestDTO) throws Exception {
		 roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.CREATE.ordinal());
		Tag tag  =tagService.create(tagRequestDTO);
		TagResponseDTO tagResponseDTO = modelMapper.map(tag, TagResponseDTO.class);
		return StandardResponse.createResponse(tagResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@PutMapping("/{id}")
	public StandardResponse updateTag(@RequestBody @Valid  TagRequestDTO tagRequestDTO,
									   @PathVariable(value = "id")Integer id) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.UPDATE.ordinal());
		Tag tag  = tagService.update(tagRequestDTO, id);
		TagResponseDTO tagResponseDTO = modelMapper.map(tag, TagResponseDTO.class);
		return StandardResponse.createResponse(tagResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@GetMapping
	public StandardResponse getTags(Pageable pageable) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		Page<TagResponseDTO> page = tagService.findAll(pageable).map(tag ->
				modelMapper.map(tag, TagResponseDTO.class));
		return StandardResponse.createResponse(page,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@DeleteMapping("/{id}")
	public StandardResponse deleteTag( @PathVariable(value = "id")Integer id) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.UPDATE.ordinal());
		boolean response  = tagService.delete(id);
		return StandardResponse.createResponse(response,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}
}