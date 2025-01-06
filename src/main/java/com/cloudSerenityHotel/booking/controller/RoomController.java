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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@GetMapping("/test")
	public List<Map<String, Object>> test(HttpServletRequest request) {
		return roomService.getAll();
	}

	@GetMapping("/type")
	public List<Map<String, Object>> getAllRoomType()
			throws ServletException, IOException {
		return roomService.getAllRoomTypes();
	}

	@PostMapping(path = "/type", consumes = "multipart/form-data")
	public Integer insertRoomType(@RequestPart String roomTypeJson,
			@RequestPart(required = false) MultipartFile typePrimaryImg,
			@RequestPart(required = false) MultipartFile[] typeImg
			)
	{
		
		RoomType roomType = null;
		try {
			roomType = objectMapper.readValue(roomTypeJson, RoomType.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return roomService.insertRoomTypeAndImg(roomType, typePrimaryImg, typeImg);	
	}

	@GetMapping("/type/{typeId}")
	public Map<String, Object> getOneRoomType(@PathVariable Integer typeId) {
		return roomService.getRoomTypeAndImgById(typeId);
	}

	
	@DeleteMapping("/type/{typeId}")
	public Integer deleteRoomType(@PathVariable Integer typeId) {
		
		return roomService.deleteRoomTypeById(typeId);
		
	}

	@PutMapping(path = "/type", consumes = "multipart/form-data")
	public Integer updateRoomType(
			@RequestPart String roomTypeJson,
			@RequestPart MultipartFile typePrimaryImg,
			@RequestPart MultipartFile[] typeImg,
			@RequestParam(required = false) String deletePrImgIdAndUrl,
			@RequestParam(required = false) String[] deleteOtherImgsIdAndUrl)
	{
		RoomType roomType = null;
		
		try {
			roomType = objectMapper.readValue(roomTypeJson, RoomType.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(deletePrImgIdAndUrl != null) {
			roomService.deleteImgById(deletePrImgIdAndUrl);
		}
		
		if(deleteOtherImgsIdAndUrl != null) {
			for(String imgIdAndUrl : deleteOtherImgsIdAndUrl) {
				roomService.deleteImgById(imgIdAndUrl);
			}
		}
		
		int row = roomService.updateRoomType(roomType);
		
		roomService.insertRoomTypeImg(roomType.getTypeId(), typePrimaryImg, typeImg);
		
		return row;
	}
	
	@PostMapping("")
	public Integer insertRoom(@RequestBody Room room) {
		return roomService.insertRoom(room);
	}

	@GetMapping("")
	public List<Map<String, Object>> getAllRooms(){
		return roomService.getAllRooms();
	}
	
	@DeleteMapping("{roomId}")
	public Integer deleteRoom(@PathVariable Integer roomId) {
		return roomService.deleteRoom(roomId);
	}
	
	@GetMapping("{roomId}")
	public Map<String, Object> getOneRoom(@PathVariable Integer roomId){
		
		return roomService.getOneRoom(roomId);
	}
	
	@PutMapping("")
	public Integer updateRoom(@RequestBody Room room) {
		return roomService.updateRoom(room);
	}
}
