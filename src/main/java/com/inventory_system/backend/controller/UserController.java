package com.inventory_system.backend.controller;

import com.inventory_system.backend.model.User;
import com.inventory_system.backend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	TokenService tokenService;

	@PostMapping("login")
	public User login(@RequestParam("user") String username, @RequestParam("password") String pwd) {
		String token = tokenService.getJWTToken(username);
		return new User(username, token);
	}


}