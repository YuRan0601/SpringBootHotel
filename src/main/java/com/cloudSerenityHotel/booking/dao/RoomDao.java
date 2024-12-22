package com.cloudSerenityHotel.booking.dao;

import java.util.List;
import com.cloudSerenityHotel.booking.model.Room;



public interface RoomDao {
	
	int insertRoom(Room room, Integer roomTypeId);

	int deleteRoomById(int roomId);

	List<Room> selectAllRooms();

	Room selectRoomById(int roomId);

	int updateRoom(Room room, int roomTypeId);			

}
