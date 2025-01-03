package com.cloudSerenityHotel.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/user")
public class LoginSystemController {

	@Autowired
	private UserService uService;

	@GetMapping("/login")
	public String login() {
		return "/user/login.jsp"; //登入頁面 進入點
	}

	@PostMapping("/checklogin")
	public String checklogin(@RequestParam String email, @RequestParam String password, HttpSession session,
			Model model) {
		User user = uService.login(email, password);
		if (user != null) { // 判斷帳號是否存在
			String status = user.getUserStatus();
			if (status.equals("Logged_out")) { // 判斷帳號是否已註銷
				model.addAttribute("errorMessage", " 該帳號已註銷，有問題請詢問客服!");
				return "/user/login.jsp";
			} else if (status.equals("In_use")) { // 狀態使用中 檢查身分組轉發到符合身分組的頁面
				String identity = user.getUserIdentity();
				session.setAttribute("identity", identity);
				session.setAttribute("userName", user.getUserName());
				session.setAttribute("userId", user.getUserId());
				
				// 檢查身分組
				if (identity.equals("admin")) { //管理員
					return "redirect:/static/common/adminPage.html";
				} else if (identity.equals("user")) { //會員
					return "/user/protected/userDashboard.jsp";
				} else { //除admin和user以外的 都是異常身分組
					model.addAttribute("errorMessage", " 該帳號出現問題，請詢問客服!");
					return "/user/login.jsp";
				}
				
			} else { // 非註銷非使用中 屬於狀態異常
				model.addAttribute("errorMessage", " 該帳號出現問題，請詢問客服!");
				return "/user/login.jsp";
			}
		} else { // 不存在 為帳密錯誤
			model.addAttribute("errorMessage", " 錯誤的Email或密碼");
			return "/user/login.jsp";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		if (session != null) {
			session.invalidate(); // 使當前 Session 無效化
		}
		// 重定向到登入頁面
		return "redirect:/user/login";
	}

	@GetMapping("/getUserInfo")
	public ResponseEntity<Map<String, Object>> getUserInfo(HttpSession session) {
	    Map<String, Object> result = uService.getUserInfo(session);
	    return ResponseEntity.ok(result);
	}

}
