package com.cloudSerenityHotel.booking.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import com.cloudSerenityHotel.booking.dao.RoomTypeDao;
import com.cloudSerenityHotel.booking.dao.RoomTypeImgDao;
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
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
	private String prefix = "http://localhost:8080/CloudSerenityHotel/static/booking/upload/imgs/";
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
		
		map.put("maxCapacity", roomType.getMaxCapacity());
		map.put("createdDate", sdf.format(roomType.getCreatedDate()));
		map.put("updatedDate", sdf.format(roomType.getUpdatedDate()));
		
		return map;
	}
	
	private List<Map<String, Object>> roomTypeToMapList(List<RoomType> roomTypes) {
		List<Map<String, Object>> res = new ArrayList<>();
		
		for (RoomType roomType : roomTypes) {
			res.add(roomTypeToMap(roomType));
		}
		
		return res;
	}
	
	
	@Override
	public List<Map<String, Object>> getAllRoomTypes() {
		List<RoomType> roomTypes = roomTypeDao.getAllRoomTypes();
		
		return roomTypeToMapList(roomTypes);
	}

	private List<RoomTypeImg> uploadImgs(Collection<Part> parts, String imgPath) {
		List<RoomTypeImg> imgs = new ArrayList<RoomTypeImg>();
		
		for(Part part : parts) {
			if(part.getSubmittedFileName() == null || part.getSubmittedFileName().equals("")) {
				continue;
			}
			System.out.println(part.getName());
			System.out.println(part.getSubmittedFileName());
			String cd = part.getHeader("Content-Disposition");
			
			String suffix = cd.substring(cd.lastIndexOf("."), cd.length() - 1);
			System.out.println(suffix);
			String filename = UUID.randomUUID().toString() + suffix;
			
			String imgUrl = prefix + filename;
			if(part.getName().equals("typePrimaryImg")) {
				RoomTypeImg img = new RoomTypeImg(null, null, imgUrl, true, null, null);
				imgs.add(img);
			} else if(part.getName().equals("typeImg")) {
				RoomTypeImg img = new RoomTypeImg(null, null, imgUrl, false, null, null);
				imgs.add(img);
			}
			
			try {
				part.write(imgPath + filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return imgs;
	}


	
	@Override
	public int insertRoomTypeAndImg(RoomType roomType, Collection<Part> parts, String imgPath) {	
		List<RoomTypeImg> imgs = uploadImgs(parts, imgPath);
		
		return roomTypeDao.insertRoomTypeAndImg(roomType, imgs);
	}
	
	

	@Override
	public int insertRoomTypeAndImg(RoomType roomType, MultipartFile typePrimaryImg, MultipartFile[] typeImg) {
		List<RoomTypeImg> imgs = uploadImgs(typePrimaryImg, typeImg);
		return roomTypeDao.insertRoomTypeAndImg(roomType, imgs);
	}

	private List<RoomTypeImg> uploadImgs(MultipartFile typePrimaryImg, MultipartFile[] typeImg) {
		List<RoomTypeImg> imgs = new ArrayList<>();
		
		if(!typePrimaryImg.isEmpty()) {
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
			String imgUrl = prefix + fileName;
			imgs.add(new RoomTypeImg(null, null, imgUrl, true, null, null));
		}
		
		
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
			String imgUrl = prefix + fileName;
			imgs.add(new RoomTypeImg(null, null, imgUrl, false, null, null));
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
		res.put("imgs", roomTypeImgToMapList(roomType.getImgs()));
		
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
		
		String[] splitImgIdAndUrl = imgIdAndUrl.split(",");
		
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
		map.put("price", room.getPrice());
		
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
			map.put("price", room.getPrice());
			
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
	
}
