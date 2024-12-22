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

@WebServlet("/user/QueryData")
public class QueryData extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public QueryData() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService userService = new UserService();
		String queryConditions = request.getParameter("conditions"); //條件(userId,userName)
		String queryTargetIdentity = request.getParameter("targetIdentity"); //身分(admin,user)
		String queryKeyword = request.getParameter("keyword"); //關鍵字
		System.out.println(queryConditions+" "+ queryTargetIdentity + " " +queryKeyword);
		//先判條件 再判身分
		if (queryConditions.equals("userId")) { //userId
			if(queryTargetIdentity.equals("admin")) { //admin
				User userData = userService.findUserById(Integer.parseInt(queryKeyword));
				List<User> data = new ArrayList<User>();
				data.add(userData);
				request.setAttribute("userData", data);
				request.getRequestDispatcher("/static/user/protected/queryResultsAdmin.jsp").forward(request, response);
			}else { //user
				User memberData = userService.findMemberById(Integer.parseInt(queryKeyword));
				List<User> data = new ArrayList<User>();
				data.add(memberData);
				request.setAttribute("memberData", data);
				request.getRequestDispatcher("/static/user/protected/queryResultsMember.jsp").forward(request, response);
			}
		}else { //userName
			if(queryTargetIdentity.equals("admin")) { //admin
				List<User> dataList = userService.findUserByName(queryKeyword);
				request.setAttribute("userData", dataList);
				request.getRequestDispatcher("/static/user/protected/queryResultsAdmin.jsp").forward(request, response);
			}else { //user
				List<User> memberDataList = userService.findMemberByName(queryKeyword);
				request.setAttribute("memberData", memberDataList);
				request.getRequestDispatcher("/static/user/protected/queryResultsMember.jsp").forward(request, response);
			}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
