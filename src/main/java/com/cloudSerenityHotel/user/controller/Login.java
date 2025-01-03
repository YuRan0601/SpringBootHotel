package com.cloudSerenityHotel.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.service.UserService;


//@WebServlet("/user/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserService userService = new UserService();
		HttpSession session = request.getSession();
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		User user = userService.login(email, password);
		if (user != null) { // 判斷帳號是否存在
			String status = user.getUserStatus();
			if (status.equals("Logged_out")) { // 判斷帳號是否已註銷
				request.setAttribute("errorMessage", " 該帳號已註銷，有問題請詢問客服!");
				request.getRequestDispatcher("/static/user/login.jsp").forward(request, response);
			} else if (status.equals("In_use")) { // 狀態使用中 檢查身分組轉發到符合身分組的頁面
				String identity = user.getUserIdentity();
				String userName = user.getUserName();
				Integer userId = user.getUserId();
				session.setAttribute("identity", identity); //設定身分組 session
				session.setAttribute("userName", userName); //設定使用者名稱 session
				session.setAttribute("userId", userId); //設定使用者編號 session
				
				// 檢查身分組
				if (identity.equals("admin")) { //管理員
					response.sendRedirect("/CloudSerenityHotel/static/common/adminPage.html");
					//request.getRequestDispatcher("/static/user/protected/adminDashBoard.jsp").forward(request, response);
				} else if (identity.equals("user")) { //會員
					request.getRequestDispatcher("/static/user/protected/userDashboard.jsp").forward(request, response);
				} else { //除admin和user以外的 都是異常身分組
					request.setAttribute("errorMessage", " 該帳號出現問題，請詢問客服!");
					request.getRequestDispatcher("/static/user/login.jsp").forward(request, response);
				}

			} else { // 非註銷非使用中 屬於狀態異常
				request.setAttribute("errorMessage", " 該帳號出現問題，請詢問客服!");
				request.getRequestDispatcher("/static/user/login.jsp").forward(request, response);
			}
		} else { // 不存在 為帳密錯誤
			request.setAttribute("errorMessage", " 錯誤的Email或密碼");
			request.getRequestDispatcher("/static/user/login.jsp").forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
