package com.cloudSerenityHotel.booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloudSerenityHotel.booking.service.BookingService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


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
}
