package com.cloudSerenityHotel.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService uService;
	
	@GetMapping("/users")
	public List<User> getAllUser() {
		return uService.findAllUser();
	}
	
	@GetMapping("/members")
	public List<User> getAllMember() {
		return uService.findAllMember();
	}
	
}
