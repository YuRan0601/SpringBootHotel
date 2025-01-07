package com.cloudSerenityHotel.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService uService;
	
	@GetMapping("/addAdmin")
	public String showAddAdmin() {
		return "/user/protected/addAdmin.jsp";
	}
	
	@PostMapping("/addAdmin") //新增管理員帳號
	public String addAdmin(@RequestParam String email,@RequestParam String password,@RequestParam String name,HttpSession session,
			Model model) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setUserName(name);
		user.setUserIdentity("admin");
		int addStatus = uService.addAdmin(user);
		if (addStatus != 0) {
			return "/user/protected/queryAdminData.jsp";
		}else {
            model.addAttribute("errorMessage", "註冊失敗，請檢查輸入的Email是否重複或稍後重試");
            return "/user/protected/addAdmin.jsp";
		}
	}
	
	@GetMapping("/queryAllAdmin") //查詢所有管理員資料
	public String queryAllAdmin(HttpSession session,Model model) {
		
			List<User> dataList = uService.findAllUser();
			model.addAttribute("userData", dataList);
			return "/user/protected/queryResultsAdmin.jsp";
	}
	
	@GetMapping("/queryAllMember") //查詢所有會員資料
	public String queryAllMember(HttpSession session,Model model) {
		
			List<User> memberDataList = uService.findAllMember();
			model.addAttribute("memberData", memberDataList);
			return "/user/protected/queryResultsMember.jsp";
	}
	
	@PostMapping("/queryAdminById")
	public String queryAdminById(@RequestParam int id,HttpSession session,Model model) {
		User userData = uService.findUserById(id);
		List<User> data = new ArrayList<User>();
		if (userData != null) {
			data.add(userData);
		}
		model.addAttribute("userData", data);
		return "/user/protected/queryResultsAdmin.jsp";
	}
	
	@PostMapping("/queryAdminByName")
	public String queryAdminByName(@RequestParam String name,HttpSession session,Model model) {
		List<User> dataList = uService.findUserByName(name);
		model.addAttribute("userData", dataList);
		return "/user/protected/queryResultsAdmin.jsp";
	}
	
	@PostMapping("/queryMemberById")
	public String queryMemberById(@RequestParam int id,HttpSession session,Model model) {
		User memberData = uService.findMemberById(id);
		List<User> data = new ArrayList<User>();
		if (memberData != null) {
			data.add(memberData);
		}
		model.addAttribute("memberData", data);
		return "/user/protected/queryResultsMember.jsp";
	}
	
	@PostMapping("/queryMemberByName")
	public String queryMemberByName(@RequestParam String name,HttpSession session,Model model) {
		List<User> memberDataList = uService.findMemberByName(name);
		model.addAttribute("memberData", memberDataList);
		return "/user/protected/queryResultsMember.jsp";
	}
	
	
}
