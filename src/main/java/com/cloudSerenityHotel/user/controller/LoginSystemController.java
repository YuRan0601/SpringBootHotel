package com.cloudSerenityHotel.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.service.UserService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = {"http://localhost:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
@Controller
@RequestMapping("/user")
public class LoginSystemController {

	@Autowired
	private UserService uService;

	@GetMapping("/login")
	public String login() {
		return "/user/login.html"; //登入頁面 進入點
	}
	
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user logged in");
    }

	@PostMapping("/checklogin")
	@ResponseBody
	public ResponseEntity<String> checklogin(@RequestBody Map<String, String> loginData, HttpSession session) {
		String email = loginData.get("email");
		String password = loginData.get("password");
//		System.out.println(loginData);
		
		User user = uService.login(email, password);
		if (user != null) { // 判斷帳號是否存在
			String status = user.getUserStatus();
			if (status.equals("Logged_out")) { // 判斷帳號是否已註銷
				String errorMessage = "該帳號已註銷，有問題請詢問客服!";
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			} else if (status.equals("In_use")) { // 狀態使用中 檢查身分組轉發到符合身分組的頁面
				String identity = user.getUserIdentity();
				User userInfo = new User();
				userInfo.setUserId(user.getUserId());
				userInfo.setUserName(user.getUserName());
				userInfo.setUserIdentity(identity);
				session.setAttribute("user",userInfo);
				
				// 檢查身分組
				if (identity.equals("admin")) { //管理員
					return ResponseEntity.ok("admin");
				} else if (identity.equals("user")) { //會員
					return ResponseEntity.ok("member");
				} else { //除admin和user以外的 都是異常身分組
					String errorMessage = "該帳號出現問題，請詢問客服!";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}
				
			} else { // 非註銷非使用中 屬於狀態異常
				String errorMessage = "該帳號出現問題，請詢問客服!";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
		} else { // 不存在 為帳密錯誤
			String errorMessage =  "錯誤的Email或密碼";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}

	@GetMapping("/logout")
	public void logout(HttpSession session) {
		if (session != null) {
			session.invalidate(); // 使當前 Session 無效化
		}
	}
	
	@PostMapping("/checkEmail")
	@ResponseBody
	public String checkEmail(@RequestBody Map<String, String> email){
		String emailStr = email.get("email");
		int checkStatus = uService.checkEmail(emailStr);
		if (checkStatus != 0) {//沒有人使用回傳1
			return "ok";
		}else {
			return "used";
		}
	}

	@GetMapping("/getUserInfo")
	@ResponseBody
	public Map<String, Object> getUserInfo(HttpSession session) {
	    Map<String, Object> result = uService.getUserInfo(session);
	    return result;
	}

}
