package com.cloudSerenityHotel.booking.service;

import java.util.List;
import java.util.Map;

import com.cloudSerenityHotel.booking.model.BookingOrder;

public interface BookingService {

	List<Map<String, Object>> getAllOrders();

	Map<String, Object> insertOrder(BookingOrder order, Integer roomTypeId);

}
