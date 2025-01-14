package com.cloudSerenityHotel.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@Controller
public class MemberController {

	@Autowired
	private UserService uService;
	
	@GetMapping("/user/register")
	public String showRegister() {
		return "/user/register.html"; //註冊頁面 進入點
	}
	
	@PostMapping("/user/register")
	public ResponseEntity<String> register(@RequestBody User member) {
		
		int registerStatus = uService.register(member,member.getMember());
		
		if(registerStatus != 0) { //註冊失敗 0 註冊成功 1
			//成功就導向登入頁面
			return ResponseEntity.ok("註冊成功");
		}else {
			//失敗就轉發回註冊頁面
		    String errorMessage = "註冊失敗";
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}
}
