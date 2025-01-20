package com.cloudSerenityHotel.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudSerenityHotel.booking.model.BookingOrder;

public interface BookingOrderRepository extends JpaRepository<BookingOrder, Integer> {

}
