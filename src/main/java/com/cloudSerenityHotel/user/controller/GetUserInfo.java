package com.cloudSerenityHotel.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

import com.cloudSerenityHotel.user.service.UserService;
import com.cloudSerenityHotel.user.service.UserServiceInterface;
import com.google.gson.Gson;

@WebServlet("/user/getUserInfo")
public class GetUserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	UserServiceInterface userService = new UserService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, Object> result = userService.getUserInfo(request.getSession());
		Gson gson = new Gson();
		
		String json = gson.toJson(result);
		
		response.getWriter().print(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
