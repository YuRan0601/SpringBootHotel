package com.cloudSerenityHotel.booking.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudSerenityHotel.booking.dao.RoomDao;
import com.cloudSerenityHotel.booking.dao.RoomRepository;
import com.cloudSerenityHotel.booking.dao.RoomTypeDao;
import com.cloudSerenityHotel.booking.dao.RoomTypeImgDao;
import com.cloudSerenityHotel.booking.dao.RoomTypeRepository;
import com.cloudSerenityHotel.booking.dao.impl.RoomDaoImpl;
import com.cloudSerenityHotel.booking.dao.impl.RoomTypeDaoImpl;
import com.cloudSerenityHotel.booking.dao.impl.RoomTypeImgDaoImpl;
import com.cloudSerenityHotel.booking.model.Room;
import com.cloudSerenityHotel.booking.model.RoomType;
import com.cloudSerenityHotel.booking.model.RoomTypeImg;
import com.cloudSerenityHotel.booking.service.RoomService;

import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private RoomTypeDao roomTypeDao;
	
	@Autowired
	private RoomTypeImgDao roomTypeImgDao;
	
	@Autowired
	private RoomTypeRepository roomTypeRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
	private String imgUrlPrefix = "http://localhost:8080/CloudSerenityHotel/static/booking/upload/imgs/";
	private String uploadPath = "src/main/webapp/static/booking/upload/imgs/";
	
	private List<Map<String, Object>> roomTypeImgToMapList(List<RoomTypeImg> imgs) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		
		for (RoomTypeImg img : imgs) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("imgId", img.getImgId());
			map.put("imgUrl", img.getImgUrl());
			map.put("isPrimary", img.getIsPrimary());
			map.put("createdDate", img.getCreatedDate());
			map.put("updatedDate", img.getUpdatedDate());
			
			mapList.add(map);
		}
		
		
		return mapList;
	}
	
	private Map<String, Object> roomTypeToMap(RoomType roomType) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("typeId", roomType.getTypeId());
		map.put("typeName", roomType.getTypeName());
		
		if(roomType.getTypeDesc() == null) {
			map.put("typeDesc", "");
		} else {
			map.put("typeDesc", roomType.getTypeDesc());
		}
		
		map.put("price", roomType.getPrice());
		map.put("maxCapacity", roomType.getMaxCapacity());
		map.put("createdDate", sdf.format(roomType.getCreatedDate()));
		map.put("updatedDate", sdf.format(roomType.getUpdatedDate()));
		
		return map;
	}
	
	private List<Map<String, Object>> roomTypeToMapList(List<RoomType> roomTypes) {
		List<Map<String, Object>> res = new ArrayList<>();
		
		for (RoomType roomType : roomTypes) {
			Map<String, Object> roomTypeMap = roomTypeToMap(roomType);
			
			List<Map<String, Object>> imgMapList = roomTypeImgToMapList(roomType.getImgs());
			
			for (Map<String, Object> map : imgMapList) {
				if((boolean)map.get("isPrimary")) {
					//主圖片裝入prImg
					roomTypeMap.put("prImg", map);
				} else {
					//其他圖片裝入imgs
					if(!roomTypeMap.containsKey("imgs")) {
						roomTypeMap.put("imgs", new ArrayList<Map<String, Object>>());
					}
					
					ArrayList list = (ArrayList)roomTypeMap.get("imgs");
					list.add(map);
				}
			}

			res.add(roomTypeMap);
		}
		
		return res;
	}
	
	public List<Map<String, Object>> getAll() {
		List<RoomType> all = roomTypeRepository.findAll();
		return roomTypeToMapList(all);
	}
	
	
	@Override
	public List<Map<String, Object>> getAllRoomTypes() {
		List<RoomType> roomTypes = roomTypeDao.getAllRoomTypes();
		
		return roomTypeToMapList(roomTypes);
	}
	
	@Override
	public int insertRoomTypeAndImg(RoomType roomType, MultipartFile typePrimaryImg, MultipartFile[] typeImg) {
		List<RoomTypeImg> imgs = uploadImgs(typePrimaryImg, typeImg);
		return roomTypeDao.insertRoomTypeAndImg(roomType, imgs);
	}

	private List<RoomTypeImg> uploadImgs(MultipartFile typePrimaryImg, MultipartFile[] typeImg) {
		List<RoomTypeImg> imgs = new ArrayList<>();
		
		if(typeImg != null) {
			String originalFilename = typePrimaryImg.getOriginalFilename();
			String fileName =  UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
			File file = new File(uploadPath + fileName);
			try {
				typePrimaryImg.transferTo(file.getAbsoluteFile());
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String imgUrl = imgUrlPrefix + fileName;
			imgs.add(new RoomTypeImg(null, null, imgUrl, true, null, null));
		}
		
		if(typeImg != null && typeImg.length != 0) {
			for (MultipartFile img : typeImg) {
				if(img.isEmpty()) {
					continue;
				}
				String originalFilename = img.getOriginalFilename();
				String fileName =  UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
				File file = new File(uploadPath + fileName);
				try {
					img.transferTo(file.getAbsoluteFile());
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String imgUrl = imgUrlPrefix + fileName;
				imgs.add(new RoomTypeImg(null, null, imgUrl, false, null, null));
			}	
		}
		
		
		return imgs;
	}

	@Override
	public int insertRoomTypeImg(Integer typeId, MultipartFile typePrimaryImg, MultipartFile[] typeImg) {
		List<RoomTypeImg> imgs = uploadImgs(typePrimaryImg, typeImg);
		
		return roomTypeImgDao.insertRoomTypeImg(typeId, imgs);
	}


	@Override
	public Map<String, Object> getRoomTypeAndImgById(int roomTypeId) {
		Map<String, Object> res = new HashMap<String, Object>();
		
		RoomType roomType = roomTypeDao.selectTypeAndImgsById(roomTypeId);
		Map<String, Object> roomTypeMap = roomTypeToMap(roomType);
		
		res.put("roomType", roomTypeMap);
		
		List<Map<String, Object>> imgMapList = roomTypeImgToMapList(roomType.getImgs());
		
		for (Map<String, Object> map : imgMapList) {
			if((boolean)map.get("isPrimary")) {
				//主圖片裝入prImg
				roomTypeMap.put("prImg", map);
			} else {
				//其他圖片裝入imgs
				if(!roomTypeMap.containsKey("imgs")) {
					roomTypeMap.put("imgs", new ArrayList<Map<String, Object>>());
				}
				
				ArrayList list = (ArrayList)roomTypeMap.get("imgs");
				list.add(map);
			}
		}
		
		return res;
	}

	private void deleteImgFile(String imgUrl) {
		String[] imgUrlSplit = imgUrl.split("/");
		
		String imgName = imgUrlSplit[imgUrlSplit.length - 1];
		
		File imgFile = new File(uploadPath + imgName);
		
		imgFile.delete();
	}
	
	@Override
	public int deleteRoomTypeById(int typeId) {
		RoomType roomType = roomTypeDao.selectTypeAndImgsById(typeId);
		
		for (RoomTypeImg img : roomType.getImgs()) {
			deleteImgFile(img.getImgUrl());
		}
		
		roomTypeImgDao.deleteImgByTypeId(typeId);
		
		return roomTypeDao.deleteRoomTypeById(typeId);
	}

	@Override
	public int updateRoomType(RoomType roomType) {
		roomType.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		return roomTypeDao.updateRoomType(roomType);
	}

	@Override
	public int deleteImgById(String imgIdAndUrl) {
		
		String[] splitImgIdAndUrl = imgIdAndUrl.split("，");
		
		int imgId = Integer.parseInt(splitImgIdAndUrl[0]);
		
		String imgUrl = splitImgIdAndUrl[1];
		
		deleteImgFile(imgUrl);
		
		return roomTypeImgDao.deleteImgById(imgId);
	}

	@Override
	public int insertRoom(Room room) {
		return roomDao.insertRoom(room);
	}
	
	@Override
	public List<Map<String, Object>> getAllRooms() {
		List<Room> rooms = roomDao.selectAllRooms();
		
		return roomToMapList(rooms);
	}
	
	private Map<String, Object> roomToMap(Room room) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("roomId", room.getRoomId());
		map.put("roomTypeId", room.getRoomType().getTypeId());
		map.put("roomTypeName", room.getRoomType().getTypeName());
		map.put("roomName", room.getRoomName());
		map.put("roomDescription", room.getRoomDescription());
		
		if(room.getStatus().equals("available")) {
			map.put("status", "空閒中");
		} else if(room.getStatus().equals("occupied")) {
			map.put("status", "占用中");
		} else if(room.getStatus().equals("maintenance")) {
			map.put("status", "修整中");
		}
		
		map.put("createdDate", sdf.format(room.getCreatedDate()));
		map.put("updatedDate", sdf.format(room.getUpdatedDate()));
		
		return map;
	}

	private List<Map<String, Object>> roomToMapList(List<Room> rooms) {
		List<Map<String, Object>> result = new ArrayList<>();
		
		for (Room room : rooms) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("roomId", room.getRoomId());
			map.put("roomTypeId", room.getRoomType().getTypeId());
			map.put("roomTypeName", room.getRoomType().getTypeName());
			map.put("roomName", room.getRoomName());
			map.put("roomDescription", room.getRoomDescription());
			
			if(room.getStatus().equals("available")) {
				map.put("status", "空閒中");
			} else if(room.getStatus().equals("occupied")) {
				map.put("status", "占用中");
			} else if(room.getStatus().equals("maintenance")) {
				map.put("status", "修整中");
			}
			
			map.put("createdDate", sdf.format(room.getCreatedDate()));
			map.put("updatedDate", sdf.format(room.getUpdatedDate()));
			
			result.add(map);
		}
		
		return result;
	}

	@Override
	public int deleteRoom(int roomId) {
		return roomDao.deleteRoomById(roomId);
	}
	
	@Override
	public Map<String, Object> getOneRoom(int roomId) {
		Room room = roomDao.selectRoomById(roomId);
		
		return roomToMap(room);
	}

	@Override
	public int updateRoom(Room room) {
		room.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		return roomDao.updateRoom(room);
	}

	@Override
	public List<Map<String, Object>> getAwailableRoomTypesAndRoomCountWithinDates(LocalDate checkInDate,
			LocalDate checkOutDate) {
		List<Map<String, Object>> typesAndRoomCountList = roomRepository.findAvailableRoomTypesAndRoomCountsWithinDates(checkInDate, checkOutDate);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		
		for (Map<String, Object> map : typesAndRoomCountList) {
			HashMap<String, Object> modifiedMap = new HashMap<>(map);
			
			mapList.add(modifiedMap);
		}
		
		for(Map<String, Object> map : mapList) {
			Integer typeId = (Integer)map.get("typeId");
			
			//透過typeId取得圖片
			List<Map<String, Object>> imgs = roomTypeImgToMapList(roomTypeImgDao.selectImgsByTypeId(typeId));
			
			for(Map<String, Object> img : imgs) {
				if((boolean)img.get("isPrimary")) {
					//主圖片裝入prImg
					map.put("prImg", img);
				} else {
					//其他圖片裝入imgs
					if(!map.containsKey("imgs")) {
						map.put("imgs", new ArrayList<Map<String, Object>>());
					}
					
					ArrayList list = (ArrayList)map.get("imgs");
					list.add(img);
				}
			}
		}
		
		return mapList;
	}

	@Override
	public List<Map<String, Object>> getRoomsByRoomType(Integer typeId) {
		List<Room> rooms = roomRepository.findByRoomType_TypeId(typeId);
		
		return roomToMapList(rooms);
	}
	
	
}
