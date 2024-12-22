package com.cloudSerenityHotel.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cloudSerenityHotel.user.model.Member;
import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.service.UserService;

@WebServlet("/user/statusLock")
public class StatusLock extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StatusLock() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserService userService = new UserService();
		String userIdStr = request.getParameter("userId");
		String action = request.getParameter("action");
		String identity = request.getParameter("identity");
		int userId = Integer.parseInt(userIdStr);
//		System.out.println(userId+" "+ action + " " +identity);
		if (action.equals("delacc")) {//delete字詞會被過濾掉
			int del = userService.deleteUser(userId);
			if (del != 0) {
				if(identity.equals("admin")) { //admin
					User userData = userService.findUserById(userId);
					List<User> data = new ArrayList<User>();
					data.add(userData);
					request.setAttribute("userData", data);
					request.getRequestDispatcher("/static/user/protected/queryResultsAdmin.jsp").forward(request, response);
				}else { //user
					User memberData = userService.findMemberById(userId);
					List<User> data = new ArrayList<User>();
					data.add(memberData);
					request.setAttribute("memberData", data);
					request.getRequestDispatcher("/static/user/protected/queryResultsMember.jsp").forward(request, response);
				}
			}else {
				request.setAttribute("errorMessage", "註銷使用者帳號");
				request.getRequestDispatcher("/static/user/protected/operationFailed.jsp").forward(request, response);
			}
		} else { // recover
			int rec = userService.recoverUser(userId);
			if (rec != 0) {
				if(identity.equals("admin")) { //admin
					User userData = userService.findUserById(userId);
					List<User> data = new ArrayList<User>();
					data.add(userData);
					request.setAttribute("userData", data);
					request.getRequestDispatcher("/static/user/protected/queryResultsAdmin.jsp").forward(request, response);
				}else { //user
					User memberData = userService.findMemberById(userId);
					List<User> data = new ArrayList<User>();
					data.add(memberData);
					request.setAttribute("memberData", data);
					request.getRequestDispatcher("/static/user/protected/queryResultsMember.jsp").forward(request, response);
				}
			}else {
				request.setAttribute("errorMessage", "恢復使用者帳號");
				request.getRequestDispatcher("/static/user/protected/operationFailed.jsp").forward(request, response);
			}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
