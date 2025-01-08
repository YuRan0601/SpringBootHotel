package com.cloudSerenityHotel.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService uService;
	
	@PostMapping("/addAdmin") //新增管理員帳號
	public ResponseEntity<String> addAdmin(@RequestBody User admin) {
		User user = new User();
		user.setEmail(admin.getEmail());
		user.setPassword(admin.getPassword());
		user.setUserName(admin.getUserName());
		user.setUserIdentity("admin");
		int addStatus = uService.addAdmin(user);
		if (addStatus != 0) {
			return ResponseEntity.ok("新增成功");
		}else {
		    String errorMessage = "新增失敗";
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}
	
	@GetMapping("/queryAllAdmin") //查詢所有管理員資料
	public String queryAllAdmin(HttpSession session) {
			return "/user/protected/queryAllAdmin.html";
	}
	
	@GetMapping("/queryAllAdminTojson") //回傳所有管理員資料
	@ResponseBody
	public ArrayList<User> queryAllAdminJson(HttpSession session,Model model) {
		
			List<User> dataList = uService.findAllUser();
			ArrayList<User> dataToJson = (ArrayList<User>) dataList;
			return dataToJson;
	}
	
	@GetMapping("/queryAllMember") //查詢所有會員資料
	public String queryAllMember(HttpSession session) {
		
			return "/user/protected/queryAllMember.html";
	}
	
	@GetMapping("/queryAllMemberTojson") //回傳所有會員資料
	@ResponseBody
	public ArrayList<User> queryAllMemberJson(HttpSession session,Model model) {
		
			List<User> memberDataList = uService.findAllMember();
			ArrayList<User> dataToJson = (ArrayList<User>) memberDataList;
			return dataToJson;
	}
	
	@PostMapping("/statusLock") //新增管理員帳號
	public ResponseEntity<String> StatusLock(@RequestBody User admin) {
		String status = admin.getUserStatus();
		if (status.equals("In_use")) {
			int del = uService.deleteUser(admin.getUserId());
			if (del != 0) {
				return ResponseEntity.ok("註銷成功");
			}else {
			    String errorMessage = "註銷失敗";
			    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
		}else {
			int rec = uService.recoverUser(admin.getUserId());
			if (rec != 0) {
				return ResponseEntity.ok("恢復成功");
			}else {
			    String errorMessage = "恢復失敗";
			    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}	
		}
	}
	
	
}
