package com.cloudSerenityHotel.booking.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.booking.dao.BookingOrderRepository;
import com.cloudSerenityHotel.booking.model.BookingOrder;
import com.cloudSerenityHotel.booking.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {
	@Autowired
	BookingOrderRepository bRepository;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
	
	private List<Map<String, Object>> ordersToMapList(List<BookingOrder> orders) {
		List<Map<String, Object>> res = new ArrayList<>();
		
		for(BookingOrder order : orders) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("orderId", order.getOrderId());
			map.put("roomId", order.getRoom().getRoomId());
			map.put("roomName", order.getRoom().getRoomName());
			map.put("checkInDate", order.getCheckInDate());
			map.put("checkOutDate", order.getCheckOutDate());
			map.put("totalPrice", order.getTotalPrice());
			map.put("status", order.getStatus());
			map.put("createdDate", sdf.format(order.getCreatedDate()));
			map.put("updatedDate", sdf.format(order.getUpdatedDate()));
			
			res.add(map);
		}
		
		return res;
	}

	@Override
	public List<Map<String, Object>> getAllOrders() {
		List<BookingOrder> orders = bRepository.findAll();
		
		return ordersToMapList(orders);
	}
	
}
