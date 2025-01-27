package com.cloudSerenityHotel.booking.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.booking.dao.BookingOrderRepository;
import com.cloudSerenityHotel.booking.dao.RoomRepository;
import com.cloudSerenityHotel.booking.model.BookingOrder;
import com.cloudSerenityHotel.booking.model.Room;
import com.cloudSerenityHotel.booking.service.BookingService;
import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.model.UserRepository;

@Service
public class BookingServiceImpl implements BookingService {
	@Autowired
	private BookingOrderRepository bRepository;
	
	@Autowired
	private RoomRepository rRepository;
	
	@Autowired
	private UserRepository uRepository;
	
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
	
	private List<Map<String, Object>> ordersToMapList(List<BookingOrder> orders) {
		List<Map<String, Object>> res = new ArrayList<>();
		
		for(BookingOrder order : orders) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("orderId", order.getOrderId());
			map.put("userId", order.getUser().getUserId());
			map.put("userName", order.getUser().getUserName());
			map.put("roomTypeId", order.getRoom().getRoomType().getTypeId());
			map.put("roomTypeName", order.getRoom().getRoomType().getTypeName());
			map.put("roomId", order.getRoom().getRoomId());
			map.put("roomName", order.getRoom().getRoomName());
			map.put("checkInDate", order.getCheckInDate());
			map.put("checkOutDate", order.getCheckOutDate());
			map.put("totalPrice", order.getTotalPrice());
			
			String status = order.getStatus();
			if(status.equals("pending")) {
				map.put("status", "待付款");
			} else if (status.equals("confirmed")){
				map.put("status", "已付款");
			} else if (status.equals("cancelled")){
				map.put("status", "已取消");
			} else if (status.equals("completed")){
				map.put("status", "已退房");
			}
			
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

	@Override
	public Map<String, Object> insertOrder(BookingOrder order, Integer roomTypeId) {
		Map<String, Object> res = new HashMap<String, Object>();
		
		//查詢隨機1間(在住房日期與退房日期內)且(房型id為roomTypeId)且(status為available)的房間
		Optional<Room> randomRoom = rRepository.findRandomAvailableRoomByTypeAndDate(roomTypeId, order.getCheckInDate().toLocalDate(), order.getCheckOutDate().toLocalDate());
		
		if(!randomRoom.isPresent()) {
			res.put("code", 404); //404沒有房間
			return res;
		}
		
		Optional<User> user = uRepository.findById(order.getUser().getUserId());
		
		if(!user.isPresent()) {
			res.put("code", 405); //405找不到此會員
			return res;
		}
		
		order.setUser(user.get());
		order.setRoom(randomRoom.get());
		
		try {
			bRepository.save(order);
			res.put("code", 200); //200新增成功
			return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			res.put("code", 501); //501新增出錯
			return res;
		}
	}
	
	
	
}
