package com.cloudSerenityHotel.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudSerenityHotel.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService uService;
	
	
}
