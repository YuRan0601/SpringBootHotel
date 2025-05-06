package com.cloudSerenityHotel.booking.dao;

import java.util.List;
import com.cloudSerenityHotel.booking.model.RoomTypeImg;


public interface RoomTypeImgDao {

	List<RoomTypeImg> selectImgsByTypeId(int roomTypeId);

	int deleteImgByTypeId(int typeId);

	int deleteImgById(int imgId);

	int insertRoomTypeImg(Integer typeId, List<RoomTypeImg> imgs);
	
}
