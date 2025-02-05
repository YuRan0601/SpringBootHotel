package com.cloudSerenityHotel.booking.service;

import java.util.List;
import java.util.Map;

import com.cloudSerenityHotel.booking.dto.MonthlyBookingCount;
import com.cloudSerenityHotel.booking.model.BookingOrder;

public interface BookingService {

	List<Map<String, Object>> getAllOrders();

	Map<String, Object> insertOrder(BookingOrder order, Integer roomTypeId);

	List<Map<String, Object>> getOrderByUserId(Integer userId);

	List<Map<String, Object>> getOrderByUserIdAndStatus(Integer userId, String status);

	Map<String, Object> updateOrderAdmin(BookingOrder order, Integer roomTypeId);

	Map<String, Object> cancelOrder(Integer orderId);

	void paymentSuccess(int orderId);

	BookingOrder getOrderById(int orderId);

	List<MonthlyBookingCount> findMonthlyBookingCounts();

	List<Map<String, Object>> getOrderByStatus(String status);

	List<Map<String, Object>> getOrderLikeUserNameOrOrderIdByKeyword(String keyword);

	List<Map<String, Object>> getOrderByOrderIdAndUserId(Integer userId, Integer orderId);


}
