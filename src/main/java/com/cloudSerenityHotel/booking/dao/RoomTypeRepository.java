package com.cloudSerenityHotel.booking.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cloudSerenityHotel.booking.model.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
	
	@Query(value = "SELECT rt.type_name, COUNT(r.room_id) AS room_count " +
            "FROM room_type rt " +
            "LEFT JOIN room r ON rt.type_id = r.room_type_id " +
            "GROUP BY rt.type_name", 
    nativeQuery = true)
	List<Map<String, Object>> findRoomTypeRate();

	@Query(value = "SELECT rt.type_name, " +
            "CASE WHEN COUNT(r.room_id) = 0 THEN 0 " +
            "ELSE COUNT(DISTINCT bo.room_id) * 100.0 / NULLIF(COUNT(r.room_id), 0) END AS booking_rate " +
            "FROM room_type rt " +
            "LEFT JOIN room r ON rt.type_id = r.room_type_id " +
            "LEFT JOIN booking_order bo ON r.room_id = bo.room_id " +
            "AND bo.status IN ('pending', 'confirmed', 'completed') " +
            "GROUP BY rt.type_name", 
    nativeQuery = true)
	List<Map<String, Object>> findBookingRate();
	
	// 計算 RoomType 總數
    @Query("SELECT COUNT(rt) FROM RoomType rt")
    long countRoomTypes();

}
