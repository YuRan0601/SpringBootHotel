package com.cloudSerenityHotel.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cloudSerenityHotel.user.service.UserService;

@Controller
public class MemberController {

	@Autowired
	private UserService uService;
	
	@GetMapping("/user/register")
	public String showRegister() {
		return "/user/register.jsp"; //註冊頁面 進入點
	}
	
	@PostMapping("/user/register")
	public String register() {
		return "/user/register.jsp";
	}
}
