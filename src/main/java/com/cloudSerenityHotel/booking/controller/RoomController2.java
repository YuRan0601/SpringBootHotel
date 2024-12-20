package com.cloudSerenityHotel.booking.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cloudSerenityHotel.base.BaseController;
import com.cloudSerenityHotel.booking.model.Room;
import com.cloudSerenityHotel.booking.model.RoomType;
import com.cloudSerenityHotel.booking.service.RoomService;
import com.cloudSerenityHotel.booking.service.impl.RoomServiceImpl;
import com.google.gson.Gson;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/room/*")
@MultipartConfig()
public class RoomController2 extends BaseController {
	private static final long serialVersionUID = 1L;
	private WebApplicationContext context;
	
	private static RoomService roomService;
	private static Gson gson = new Gson();

	
	protected void getAllRoomType(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Map<String, Object>> roomType = roomService.getAllRoomTypes();
		
		String json = gson.toJson(roomType);
		
		response.setContentType("text/html;charset=UTF-8");
		
		response.getWriter().print(json);
	}


	protected void insertRoomType(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String typeName = request.getParameter("typeName");
		String maxCapacity = request.getParameter("maxCapacity");
		String typeDesc = request.getParameter("typeDesc");
		
		RoomType roomType = new RoomType(null, typeName, typeDesc, Integer.parseInt(maxCapacity), null, null);
		
		String imgPath = getServletContext().getRealPath("/static/booking/upload/imgs/");
		
		int row = roomService.insertRoomTypeAndImg(roomType, request.getParts(), imgPath);	
		
		if(row > 0) {
			response.sendRedirect("http://localhost:8080/CloudSerenityHotel/static/booking/roomType.html");
		}
	}


	protected void getOneRoomType(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String typeId = request.getParameter("typeId");
		
		Map<String, Object> result = roomService.getRoomTypeAndImgById(Integer.parseInt(typeId));
		
		String json = gson.toJson(result);
		
		response.getWriter().print(json);
	}


	protected void deleteRoomType(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String typeId = request.getParameter("typeId");
		
		String imgPath = getServletContext().getRealPath("/static/booking/upload/imgs/");
		
		int row = roomService.deleteRoomTypeById(Integer.parseInt(typeId), imgPath);
		
		response.setContentType("text/html;charset=UTF-8");
		
		response.getWriter().print(row);
	}

	protected void updateRoomType(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int typeId = Integer.parseInt(request.getParameter("typeId"));
		String typeName = request.getParameter("typeName");
		String typeDesc = request.getParameter("typeDesc");
		int maxCapacity = Integer.parseInt(request.getParameter("maxCapacity"));
		Collection<Part> parts = request.getParts();
		String deletePrImgIdAndUrl = request.getParameter("deletePrImgIdAndUrl");
		String[] deleteOtherImgsIdAndUrl = request.getParameterValues("deleteOtherImgsIdAndUrl");
		
		RoomType roomType = new RoomType(typeId, 
				typeName, 
				typeDesc, 
				maxCapacity, 
				null, 
				new Timestamp(new Date().getTime()));
		
		String imgPath = getServletContext().getRealPath("/static/booking/upload/imgs/");
		
		if(deletePrImgIdAndUrl != null) {
			roomService.deleteImgById(deletePrImgIdAndUrl, imgPath);
		}
		
		if(deleteOtherImgsIdAndUrl != null) {
			for(String imgIdAndUrl : deleteOtherImgsIdAndUrl) {
				roomService.deleteImgById(imgIdAndUrl, imgPath);
			}
		}
		
		int row = roomService.updateRoomType(roomType);
		
		roomService.insertRoomTypeImg(typeId, parts, imgPath);

		if(row > 0) {
			response.sendRedirect("http://localhost:8080/CloudSerenityHotel/static/booking/roomType.html");
		}
	}
	

	protected void insertRoom(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer roomTypeId = Integer.parseInt(request.getParameter("roomTypeId"));
		String roomName = request.getParameter("roomName");
		String roomDescription = request.getParameter("roomDescription");
		BigDecimal price = new BigDecimal(request.getParameter("price"));
		String status = request.getParameter("status");
		
		Room room = new Room(null, null, roomName, roomDescription, price, status, null, null);
		
		int result = roomService.insertRoom(room, roomTypeId);
		
		if(result > 0) {
			response.sendRedirect("http://localhost:8080/CloudSerenityHotel/static/booking/room.html");
		}
	}

	protected void getAllRooms(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Map<String, Object>> rooms = roomService.getAllRooms();
		
		String json = gson.toJson(rooms);
		
		response.setContentType("text/html;charset=UTF-8");
		
		response.getWriter().print(json);
	}
	
	protected void deleteRoom(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 int roomId = Integer.parseInt(request.getParameter("roomId"));
		 
		 int row = roomService.deleteRoom(roomId);
		 
		 response.getWriter().print(row);
	}
	
	protected void getOneRoom(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int roomId = Integer.parseInt(request.getParameter("roomId"));
		
		Map<String, Object> result = roomService.getOneRoom(roomId);
		
		String json = gson.toJson(result);
		
		response.setContentType("text/html;charset=UTF-8");
	 	
	 	response.getWriter().print(json);
	}
	
	protected void updateRoom(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int roomId = Integer.parseInt(request.getParameter("roomId"));
		int roomTypeId = Integer.parseInt(request.getParameter("roomTypeId"));
		String roomName = request.getParameter("roomName");
		String roomDescription = request.getParameter("roomDescription");
		BigDecimal price = new BigDecimal(request.getParameter("price"));
		String status = request.getParameter("status");
		
		Room room = new Room(roomId, null, roomName, 
				roomDescription, price, status, null, null);
		
		int row = roomService.updateRoom(room, roomTypeId);
		
		if(row > 0) {
			response.sendRedirect("http://localhost:8080/CloudSerenityHotel/static/booking/room.html");
		}
	}
	
	
	
	protected void test(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println(request.getContextPath());
		System.out.println(getServletContext().getRealPath("/static/booking/upload/imgs/"));
	}
	
	@Override
	public void init() throws ServletException {
		ServletContext application = getServletContext();
		context = WebApplicationContextUtils.getWebApplicationContext(application);
		
		roomService = context.getBean(RoomService.class);
	}
}
