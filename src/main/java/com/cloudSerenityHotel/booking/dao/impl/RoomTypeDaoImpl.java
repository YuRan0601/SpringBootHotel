package com.cloudSerenityHotel.booking.dao.impl;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.base.BaseDao;
import com.cloudSerenityHotel.booking.dao.RoomTypeDao;
import com.cloudSerenityHotel.booking.model.RoomType;
import com.cloudSerenityHotel.booking.model.RoomTypeImg;
import com.cloudSerenityHotel.utils.HibernateUtil;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public class RoomTypeDaoImpl implements RoomTypeDao {
	@Autowired
	private SessionFactory factory;
	
	@Override
	public List<RoomType> getAllRoomTypes() {
		Session session = factory.getCurrentSession();
		
		Query<RoomType> query = session.createQuery("""
				From RoomType
				""", RoomType.class);
		
		List<RoomType> roomTypes = query.getResultList();

		return roomTypes;
	}
	
	@Override
	public int insertRoomTypeAndImg(RoomType roomType, List<RoomTypeImg> imgs) {
		Session session = factory.getCurrentSession();
		
		try {
			session.persist(roomType);
			
			List<RoomTypeImg> dbImgs = roomType.getImgs();
			
			for (RoomTypeImg roomTypeImg : imgs) {
				roomTypeImg.setRoomType(roomType);
				session.persist(roomTypeImg);
				
				dbImgs.add(roomTypeImg);
			}
			
			return 1;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public RoomType selectTypeAndImgsById(int roomTypeId) {
		Session session = factory.getCurrentSession();
		
		return session.find(RoomType.class, roomTypeId);
	}

	@Override
	public int deleteRoomTypeById(int typeId) {
		Session session = factory.getCurrentSession();
		
		return session.createMutationQuery("""
				DELETE FROM RoomType
				WHERE typeId = :typeId
				""")
		.setParameter("typeId", typeId)
		.executeUpdate();
	}

	@Override
	public int updateRoomType(RoomType roomType) {
		Session session = factory.getCurrentSession();
		
		return session.createMutationQuery("""
				UPDATE RoomType
				SET
					typeName = :tName,
					typeDesc = :tDesc,
					price = :price,
					maxCapacity = :maxCapacity,
					updatedDate = :updatedDate
				WHERE typeId = :tId
				""")
		.setParameter("tName", roomType.getTypeName())
		.setParameter("tDesc", roomType.getTypeDesc())
		.setParameter("price", roomType.getPrice())
		.setParameter("maxCapacity", roomType.getMaxCapacity())
		.setParameter("updatedDate", roomType.getUpdatedDate())
		.setParameter("tId", roomType.getTypeId())
		.executeUpdate();
	}
}
