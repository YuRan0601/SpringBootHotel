package com.cloudSerenityHotel.booking.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cloudSerenityHotel.booking.model.Room;
import com.cloudSerenityHotel.booking.model.RoomType;

import jakarta.servlet.http.Part;

public interface RoomService {
	/**
	 * 獲取所有房型資料
	 * @return 所有房型資料
	 */
	List<Map<String, Object>> getAllRoomTypes();

	Map<String, Object> getRoomTypeAndImgById(int roomTypeId);
	
	List<Map<String, Object>> getAll();

	int deleteRoomTypeById(int typeId);

	int updateRoomType(RoomType roomType);

	int insertRoomTypeImg(Integer typeId, MultipartFile typePrimaryImg, MultipartFile[] typeImg);

	int deleteImgById(String imgIdAndUrl);

	int insertRoom(Room room);

	List<Map<String, Object>> getAllRooms();

	int deleteRoom(int roomId);

	Map<String, Object> getOneRoom(int roomId);

	int updateRoom(Room room);

	int insertRoomTypeAndImg(RoomType roomType, MultipartFile typePrimaryImg, MultipartFile[] typeImg);

	List<Map<String, Object>> getAwailableRoomTypesAndRoomCountWithinDates(LocalDate checkInDate,
			LocalDate checkOutDate);

	List<Map<String, Object>> getRoomsByRoomType(Integer typeId);
	
}
