package com.cloudSerenityHotel.booking.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cloudSerenityHotel.booking.dto.MonthlyBookingCount;
import com.cloudSerenityHotel.booking.model.BookingOrder;

public interface BookingOrderRepository extends JpaRepository<BookingOrder, Integer> {

	List<BookingOrder> findByUser_UserId(Integer userId);

	List<BookingOrder> findByUser_UserIdAndStatus(Integer userId, String status);
	
	@Query(value = "SELECT TOP 5 FORMAT(check_in_date, 'yyyy-MM') AS month, COUNT(*) AS booking_count " +
            "FROM booking_order " +
			"WHERE status <> 'cancelled'" +
            "GROUP BY FORMAT(check_in_date, 'yyyy-MM') " +
            "ORDER BY MAX(check_in_date) DESC", 
    nativeQuery = true)
	List<MonthlyBookingCount> findMonthlyBookingCounts();

	List<BookingOrder> findByStatus(String status);

}
