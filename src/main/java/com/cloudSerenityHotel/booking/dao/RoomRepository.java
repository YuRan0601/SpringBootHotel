package com.cloudSerenityHotel.booking.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloudSerenityHotel.booking.model.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
	 @Query("""
		        SELECT 
	 		    	rt.typeId typeId,
	 		    	rt.typeName typeName,
 		    		rt.typeDesc typeDesc,
 		    		rt.price price,
 		    		rt.maxCapacity maxCapacity,
 		     		COUNT(r.roomId) roomCount
		        FROM Room r
		        JOIN r.roomType rt
		        WHERE r.roomId NOT IN (
		            SELECT bo.room.roomId
		            FROM BookingOrder bo
		            WHERE bo.checkInDate < :checkOutDate AND bo.checkOutDate > :checkInDate
		        )
		        AND r.status = 'AVAILABLE'
		        GROUP BY rt.typeId, rt.typeName, rt.typeDesc, rt.price, rt.maxCapacity
		        ORDER BY rt.typeName
		    """)
		    List<Map<String, Object>> findAvailableRoomTypesAndRoomCountsWithinDates(
		        @Param("checkInDate") LocalDate checkInDate,
		        @Param("checkOutDate") LocalDate checkOutDate
		    );
}
