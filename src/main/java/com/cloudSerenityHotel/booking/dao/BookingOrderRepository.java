package com.cloudSerenityHotel.booking.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudSerenityHotel.booking.model.BookingOrder;

public interface BookingOrderRepository extends JpaRepository<BookingOrder, Integer> {

	List<BookingOrder> findByUser_UserId(Integer userId);

	List<BookingOrder> findByUser_UserIdAndStatus(Integer userId, String status);


}
