package com.cloudSerenityHotel.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.cloudSerenityHotel.user.model.Member;
import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.service.UserService;


@WebServlet("/user/getUpdateData")
public class GetUpdateData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetUpdateData() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService userService = new UserService();
		int userId = Integer.parseInt(request.getParameter("userId"));
		String identity = request.getParameter("identity");
		if(identity.equals("admin")) { //admin
			User user = userService.findUserById(userId);
			request.setAttribute("userData", user);
			request.getRequestDispatcher("/static/user/protected/updateAdminData.jsp").forward(request, response);
		}else { //user
			User user = userService.findMemberById(userId);
			Member member = user.getMember();
			request.setAttribute("userData", user);
			request.setAttribute("memberData", member);
			request.getRequestDispatcher("/static/user/protected/updateMemberData.jsp").forward(request, response);
		}

		
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
