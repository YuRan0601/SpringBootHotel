package com.cloudSerenityHotel.booking.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudSerenityHotel.base.BaseController;
import com.cloudSerenityHotel.booking.model.Room;
import com.cloudSerenityHotel.booking.model.RoomType;
import com.cloudSerenityHotel.booking.service.RoomService;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@RestController
@RequestMapping("/room")
@MultipartConfig
public class RoomController extends BaseController {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private RoomService roomService;
	
	@GetMapping("/test")
	public String test(HttpServletRequest request) {
		return "static/user/login.jsp";
	}

	@GetMapping("/getAllRoomType")
	public List<Map<String, Object>> getAllRoomType()
			throws ServletException, IOException {
		return roomService.getAllRoomTypes();
	}

	@PostMapping("/insertRoomType")
	public void insertRoomType(@RequestParam String typeName, 
			@RequestParam String maxCapacity, 
			@RequestParam String typeDesc,
			@RequestParam MultipartFile typePrimaryImg,
			@RequestParam MultipartFile[] typeImg,
			HttpServletRequest req,
			HttpServletResponse resp
			)
			throws ServletException, IOException {
	
		RoomType roomType = new RoomType(null, typeName, typeDesc, Integer.parseInt(maxCapacity), null, null);
		
		int row = roomService.insertRoomTypeAndImg(roomType, typePrimaryImg, typeImg);	
		
		if(row > 0) {
			resp.sendRedirect("http://localhost:8080/CloudSerenityHotel/static/booking/roomType.html");
		}
	}

	@GetMapping("/getOneRoomType")
	public Map<String, Object> getOneRoomType(@RequestParam String typeId) {
		return roomService.getRoomTypeAndImgById(Integer.parseInt(typeId));
	}

	
	@DeleteMapping("/deleteRoomType")
	public Integer deleteRoomType(@RequestParam String typeId, HttpServletResponse resp)
			throws ServletException, IOException {
		
		return roomService.deleteRoomTypeById(Integer.parseInt(typeId));
		
	}

	@PutMapping(path = "/updateRoomType")
	public void updateRoomType(
			@RequestParam String typeId,
			@RequestParam String typeName,
			@RequestParam String typeDesc,
			@RequestParam String maxCapacity,
			@RequestParam(required = false) MultipartFile typePrimaryImg,
			@RequestParam(required = false) MultipartFile[] typeImg,
			@RequestParam(required = false) String deletePrImgIdAndUrl,
			@RequestParam(required = false) String[] deleteOtherImgsIdAndUrl)
			throws ServletException, IOException {
		
		RoomType roomType = new RoomType(Integer.parseInt(typeId), 
				typeName, 
				typeDesc, 
				Integer.parseInt(maxCapacity), 
				null, 
				new Timestamp(new Date().getTime()));
		
		if(deletePrImgIdAndUrl != null) {
			roomService.deleteImgById(deletePrImgIdAndUrl);
		}
		
		if(deleteOtherImgsIdAndUrl != null) {
			for(String imgIdAndUrl : deleteOtherImgsIdAndUrl) {
				roomService.deleteImgById(imgIdAndUrl);
			}
		}
		
		int row = roomService.updateRoomType(roomType);
		
		roomService.insertRoomTypeImg(Integer.parseInt(typeId), typePrimaryImg, typeImg);

	}
	
	@PostMapping("/insertRoom")
	public void insertRoom(
			@RequestParam String roomTypeId,
			@RequestParam String roomName,
			@RequestParam String roomDescription,
			@RequestParam String price,
			@RequestParam String status,
			HttpServletResponse resp) throws IOException
	{

		
		Room room = new Room(null, 
				new RoomType(), 
				roomName, 
				roomDescription, 
				new BigDecimal(price), 
				status, 
				null, 
				null);
		
		room.getRoomType().setTypeId(Integer.parseInt(roomTypeId));
		
		roomService.insertRoom(room);
		
		resp.sendRedirect("http://localhost:8080/CloudSerenityHotel/static/booking/room.html");
		
	}

	@GetMapping("/getAllRooms")
	public List<Map<String, Object>> getAllRooms(){
		return roomService.getAllRooms();
	}
	
	@DeleteMapping("/deleteRoom")
	public Integer deleteRoom(@RequestParam String roomId) {
		 return roomService.deleteRoom(Integer.parseInt(roomId));
	}
	
	@GetMapping("/getOneRoom")
	public Map<String, Object> getOneRoom(@RequestParam String roomId){
		
		return roomService.getOneRoom(Integer.parseInt(roomId));
	}
	
	@PutMapping("/updateRoom")
	public void updateRoom(
			@RequestParam String roomId,
			@RequestParam String roomTypeId,
			@RequestParam String roomName,
			@RequestParam String roomDescription,
			@RequestParam String price,
			@RequestParam String status,
			HttpServletResponse resp
			)
			throws ServletException, IOException {
		
		Room room = new Room(Integer.parseInt(roomId) , 
				new RoomType(), 
				roomName, 
				roomDescription, 
				new BigDecimal(price), 
				status, 
				null, 
				null);
		
		room.getRoomType().setTypeId(Integer.parseInt(roomTypeId));
		
		int row = roomService.updateRoom(room);
		
		if(row > 0) {
			resp.sendRedirect("http://localhost:8080/CloudSerenityHotel/static/booking/room.html");
		}
	}
}
