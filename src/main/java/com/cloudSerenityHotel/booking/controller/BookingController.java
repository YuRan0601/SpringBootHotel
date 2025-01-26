package com.cloudSerenityHotel.booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudSerenityHotel.booking.model.BookingOrder;
import com.cloudSerenityHotel.booking.service.BookingService;

import jakarta.websocket.server.PathParam;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/booking")
@CrossOrigin(origins = {"http://localhost:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class BookingController {
	
	@Autowired
	BookingService bService;
	
	@GetMapping("/order")
	public List<Map<String, Object>> getAllOrders() {
		return bService.getAllOrders();
	}
	
	@PostMapping("/order/{roomTypeId}")
	public void insertOrder(@RequestBody BookingOrder order, @PathVariable Integer roomTypeId) {
		System.out.println(order.getUser().getUserId());
		System.out.println(order.getTotalPrice());
		System.out.println(roomTypeId);
	}
}
