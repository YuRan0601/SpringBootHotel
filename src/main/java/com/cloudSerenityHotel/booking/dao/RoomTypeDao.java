package com.cloudSerenityHotel.booking.dao;

import java.util.List;

import com.cloudSerenityHotel.booking.model.RoomType;
import com.cloudSerenityHotel.booking.model.RoomTypeImg;



public interface RoomTypeDao {

	/**
	 * 獲取所有房型資料
	 * @return 所有房型資料
	 */
	List<RoomType> getAllRoomTypes();

	int deleteRoomTypeById(int typeId);

	int updateRoomType(RoomType roomType);

	int insertRoomTypeAndImg(RoomType roomType, List<RoomTypeImg> imgs);

	RoomType selectTypeAndImgsById(int roomTypeId);

}
