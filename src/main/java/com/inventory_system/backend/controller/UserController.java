package com.inventory_system.backend.controller;

import com.inventory_system.backend.dto.request.user.UserRequestDTO;
import com.inventory_system.backend.dto.response.LoginResponseDTO;
import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.dto.response.user.UserResponseDTO;
import com.inventory_system.backend.enums.Action;
import com.inventory_system.backend.enums.Allowed;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.User;
import com.inventory_system.backend.service.RoleOperativeActionService;
import com.inventory_system.backend.service.TokenService;
import com.inventory_system.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	TokenService tokenService;
	@Autowired
	UserService userService;
	@Autowired
	RoleOperativeActionService roleOperativeActionService;
	@Autowired
	ModelMapper modelMapper;

	private final int OPERATIVE = 1;

	@PostMapping("login")
	public LoginResponseDTO login(@RequestParam("nick") String nick, @RequestParam("password") String password) throws Exception {

		User user = userService.findByNick(nick);
		//TODO: Usar aquí Clave ssh para encriptar la clave en front
		// o al menos colocar un MD5
		if(!user.getPassword().equals(password)){
			throw new UnauthorizedException();
		}
		String token = tokenService.getJWTToken(nick);
		return new LoginResponseDTO(nick, token);
	}

	@GetMapping("/{id}")
	public StandardResponse getUser(@PathVariable(value = "id")Integer id) throws Exception {
		roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		User user = userService.findById(id);
		UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);
		return StandardResponse.createResponse(userResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));

	}

	@PostMapping
	public StandardResponse createUser(@RequestBody @Valid UserRequestDTO userRequestDTO) throws Exception {
		Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.CREATE.ordinal());
		User user = userService.create(userRequestDTO,allowed);
		UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);
		return StandardResponse.createResponse(userResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@PutMapping("/{id}")
	public StandardResponse updateUser(@RequestBody @Valid UserRequestDTO userRequestDTO,
									   @PathVariable(value = "id")Integer id) throws Exception {
		Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.UPDATE.ordinal());
		User user = userService.update(userRequestDTO, id,allowed);
		UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);
		return StandardResponse.createResponse(userResponseDTO,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}

	@GetMapping
	public StandardResponse getUsers(Pageable pageable) throws Exception {
		Allowed allowed = roleOperativeActionService.checkRoleOperativeAndAction(OPERATIVE, Action.QUERY.ordinal());
		Page<UserResponseDTO> page = userService.findAll(pageable,allowed).map(user ->
				modelMapper.map(user, UserResponseDTO.class));
		return StandardResponse.createResponse(page,
				tokenService.getJWTToken(tokenService.getUserNick()));
	}
}