package com.cloudSerenityHotel.booking.service;

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

	int insertRoomTypeAndImg(RoomType roomType, Collection<Part> parts, String imgPath);

	Map<String, Object> getRoomTypeAndImgById(int roomTypeId);

	int deleteRoomTypeById(int typeId, String imgPath);

	int updateRoomType(RoomType roomType);

	int insertRoomTypeImg(Integer typeId, Collection<Part> parts, String imgPath);

	int deleteImgById(String imgIdAndUrl, String imgPath);

	int insertRoom(Room room, Integer roomTypeId);

	List<Map<String, Object>> getAllRooms();

	int deleteRoom(int roomId);

	Map<String, Object> getOneRoom(int roomId);

	int updateRoom(Room room, int roomTypeId);

	int insertRoomTypeAndImg(RoomType roomType, MultipartFile typePrimaryImg, MultipartFile[] typeImg);
	
}
