package com.cloudSerenityHotel.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.cloudSerenityHotel.user.model.Member;
import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.service.UserService;


@WebServlet("/user/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserService userService = new UserService();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String birthdayStr = request.getParameter("birthday");
		String phone = request.getParameter("phone");
		String personalIdNo = request.getParameter("personal_id_no");
		String country = request.getParameter("country");
		String address = request.getParameter("address");
		String passportNo = request.getParameter("passport_no");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate birthday = LocalDate.parse(birthdayStr , formatter);
		
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setUserName(name);
		user.setUserStatus("In_use");
		user.setUserIdentity("user");
		Member member = new Member();
		member.setGender(gender);
		member.setBirthday(birthday);
		member.setPhone(phone);
		member.setPersonalIdNo(personalIdNo);
		member.setCountry(country);
		member.setAddress(address);
		member.setPassportNo(passportNo);
		
		int registerStatus = userService.register(user, member);
		if(registerStatus != 0) { //註冊失敗 0 註冊成功 1
			//成功就導向登入頁面
			response.sendRedirect(request.getContextPath() +"/static/user/login.jsp");
		}else {
			//失敗就轉發回註冊頁面
            request.setAttribute("errorMessage", "註冊失敗，請檢查輸入資訊或稍後重試");
            request.getRequestDispatcher("/static/user/register.jsp").forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
