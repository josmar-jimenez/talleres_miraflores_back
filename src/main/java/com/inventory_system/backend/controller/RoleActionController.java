package com.inventory_system.backend.controller;

import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.dto.response.user.RoleOperativeActionResponseDTO;
import com.inventory_system.backend.model.RoleAction;
import com.inventory_system.backend.model.User;
import com.inventory_system.backend.service.RoleOperativeActionService;
import com.inventory_system.backend.service.TokenService;
import com.inventory_system.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/operatives")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RoleActionController {

	@Autowired
	TokenService tokenService;
	@Autowired
	UserService userService;
	@Autowired
	RoleOperativeActionService roleOperativeActionService;
	@Autowired
	ModelMapper modelMapper;

	@GetMapping
	public StandardResponse getUserAllowedAction() throws Exception {
		String nick = tokenService.getUserNick();
		User user = userService.findByNick(nick);
		String token = tokenService.getJWTToken(nick);

		List<RoleAction> roleOperativeAction =
				roleOperativeActionService.findByRoleId(user.getRole().getId());
		List<RoleOperativeActionResponseDTO> roleOperativeActionResponseDTOS = new ArrayList<>();
		if(!CollectionUtils.isEmpty(roleOperativeAction)){
			roleOperativeAction.forEach(roleAction ->
					roleOperativeActionResponseDTOS.add(modelMapper.map(roleAction, RoleOperativeActionResponseDTO.class)));
		}
		return StandardResponse.createResponse(roleOperativeActionResponseDTOS, token);
	}


}