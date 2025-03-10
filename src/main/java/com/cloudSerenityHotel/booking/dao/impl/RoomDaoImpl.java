package com.cloudSerenityHotel.booking.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.base.BaseDao;
import com.cloudSerenityHotel.booking.dao.RoomDao;
import com.cloudSerenityHotel.booking.model.Room;
import com.cloudSerenityHotel.booking.model.RoomType;
import com.cloudSerenityHotel.utils.HibernateUtil;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public class RoomDaoImpl implements RoomDao {
	
	@Autowired
	private SessionFactory factory;
	
	@Override
	public int insertRoom(Room room) {
		Session session = factory.getCurrentSession();
		
		try {
			RoomType roomType = session.find(RoomType.class, room.getRoomType().getTypeId());
			
			room.setRoomType(roomType);
			
			session.persist(room);
			
			List<Room> rooms = roomType.getRooms();
			
			rooms.add(room);
			
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			
			return 0;
		}
	}
	
	@Override
	public List<Room> selectAllRooms() {
		Session session = factory.getCurrentSession();
		
		return session.createQuery("FROM Room", Room.class).getResultList();
	}

	@Override
	public int deleteRoomById(int roomId) {
		Session session = factory.getCurrentSession();
		
		return session.createMutationQuery("""
				DELETE FROM Room
				WHERE roomId = :roomId
				""")
		.setParameter("roomId", roomId)
		.executeUpdate();
	}
	
	@Override
	public Room selectRoomById(int roomId) {
		Session session = factory.getCurrentSession();
		
		return session.find(Room.class, roomId);
	}

	@Override
	public int updateRoom(Room room) {
		Session session = factory.getCurrentSession();
		
		RoomType roomType = session.find(RoomType.class, room.getRoomType().getTypeId());
		
		return session.createMutationQuery("""
				UPDATE Room
				SET
					roomType = :roomType,
					roomName = :roomName,
					roomDescription = :roomDesc,
					status = :status,
					updatedDate = :updatedDate
				WHERE roomId = :roomId
				""")
		.setParameter("roomType", roomType)
		.setParameter("roomName", room.getRoomName())
		.setParameter("roomDesc", room.getRoomDescription())
		.setParameter("status", room.getStatus())
		.setParameter("updatedDate", new Timestamp(new Date().getTime()))
		.setParameter("roomId", room.getRoomId())
		.executeUpdate();
	}

}
