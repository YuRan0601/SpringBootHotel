package com.cloudSerenityHotel.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.cloudSerenityHotel.user.model.Member;
import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.service.UserService;

@WebServlet("/user/updateData")
public class UpdateData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UpdateData() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService userService = new UserService();
		User updateUser = new User();
		Integer userId = Integer.parseInt(request.getParameter("userId"));
		updateUser.setUserId(userId);
		updateUser.setUserName(request.getParameter("name"));
		updateUser.setEmail(request.getParameter("email"));
		updateUser.setPassword(request.getParameter("password"));
		String identity = request.getParameter("identity");
		updateUser.setUserIdentity(identity);
		
		if(identity.equals("admin")) { //admin
			int update = userService.updateUser(updateUser);
			if (update > 0) {
				User userData = userService.findUserById(userId);
				List<User> data = new ArrayList<User>();
				data.add(userData);
				request.setAttribute("userData", data);
				request.getRequestDispatcher("/static/user/protected/queryResultsAdmin.jsp").forward(request, response);
			}else {
				request.setAttribute("errorMessage", "更新使用者帳號");
				request.getRequestDispatcher("/static/user/protected/operationFailed.jsp").forward(request, response);
			}

		}else { //user
			String birthdayStr = request.getParameter("birthday");
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate birthday = LocalDate.parse(birthdayStr , formatter);
			
			Member member = new Member();
			member.setUserId(userId);
			member.setGender(request.getParameter("gender"));
			member.setBirthday(birthday);
			member.setPhone(request.getParameter("phone"));
			member.setPersonalIdNo(request.getParameter("personalIdNo"));
			member.setCountry(request.getParameter("country"));
			member.setAddress(request.getParameter("address"));
			member.setPassportNo(request.getParameter("passportNo"));
			updateUser.setMember(member);
			
			int update = userService.updateMember(updateUser);
			if (update > 0) {
				User memberData = userService.findMemberById(userId);
				List<User> data = new ArrayList<User>();
				data.add(memberData);
				request.setAttribute("memberData", data);
				request.getRequestDispatcher("/static/user/protected/queryResultsMember.jsp").forward(request, response);
			}else {
				request.setAttribute("errorMessage", "更新使用者帳號");
				request.getRequestDispatcher("/static/user/protected/operationFailed.jsp").forward(request, response);
			}

		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
