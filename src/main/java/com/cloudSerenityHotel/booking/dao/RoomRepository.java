package com.cloudSerenityHotel.booking.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
		            AND bo.status <> 'cancelled'
		        )
		        AND r.status = 'AVAILABLE'
		        GROUP BY rt.typeId, rt.typeName, rt.typeDesc, rt.price, rt.maxCapacity
		        ORDER BY rt.typeName
		    """)
    List<Map<String, Object>> findAvailableRoomTypesAndRoomCountsWithinDates(
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate
    );
	 
	 /**
	  * 查找指定房型 ID 且在日期範圍内未被預定的隨機房間。
	  *
	  * @param roomTypeId   房型 ID
	  * @param checkInDate  入住日期
	  * @param checkOutDate 退房日期
	  * @return 随机可用房间的 Optional 对象，如果没有符合条件的房间则返回 Optional.empty()
	  */
	@Query(value = """
			    SELECT TOP 1 r.*
			    FROM room r
			    WHERE r.room_type_id = :roomTypeId
			      AND r.status = 'available'
			      AND NOT EXISTS (
			          SELECT 1
			          FROM booking_order bo
			          WHERE bo.room_id = r.room_id
			            AND (bo.check_in_date <= :checkOutDate AND bo.check_out_date >= :checkInDate)
			      )
			    ORDER BY NEWID() -- SQL Server 隨機排序
			""", nativeQuery = true)
	Optional<Room> findRandomAvailableRoomByTypeAndDate(
		@Param("roomTypeId") Integer roomTypeId,
	    @Param("checkInDate") LocalDate checkInDate,
	    @Param("checkOutDate") LocalDate checkOutDate
	);

	List<Room> findByRoomType_TypeId(Integer roomTypeId);
}
