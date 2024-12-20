package com.cloudSerenityHotel.booking.dao.impl;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.base.BaseDao;
import com.cloudSerenityHotel.booking.dao.RoomTypeImgDao;
import com.cloudSerenityHotel.booking.model.RoomType;
import com.cloudSerenityHotel.booking.model.RoomTypeImg;
import com.cloudSerenityHotel.utils.HibernateUtil;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public class RoomTypeImgDaoImpl extends BaseDao implements RoomTypeImgDao {
	@Autowired
//	@Qualifier("customSessionFactory")
	private SessionFactory factory;
	
//	public RoomTypeImgDaoImpl() {
//		this.factory = HibernateUtil.getSessionFactory();
//	}

	@Override
	public List<RoomTypeImg> selectImgsByTypeId(int roomTypeId) {
		Session session = factory.getCurrentSession();
		
		return session.createQuery("""
				FROM RoomTypeImg
				WHERE roomType.typeId = :tid
				""", RoomTypeImg.class)
		.setParameter("tid", roomTypeId)
		.getResultList();

	}
	
	@Override
	public int deleteImgByTypeId(int typeId) {
		
		Session session = factory.getCurrentSession();
		
		return session.createMutationQuery("""
				DELETE FROM RoomTypeImg
				WHERE roomType.typeId = :typeId
				""")
		.setParameter("typeId", typeId)
		.executeUpdate();
	}

	@Override
	public int deleteImgById(int imgId) {
		Session session = factory.getCurrentSession();
		
		return session.createMutationQuery("""
				DELETE FROM RoomTypeImg
				WHERE imgId = :imgId
				""")
		.setParameter("imgId", imgId)
		.executeUpdate();
	}

	@Override
	public int insertRoomTypeImg(Integer typeId, List<RoomTypeImg> imgs) {
		Session session = factory.getCurrentSession();
		
		try {
			RoomType roomType = session.find(RoomType.class, typeId);
			
			List<RoomTypeImg> dbImgs = roomType.getImgs();
			
			for (RoomTypeImg img : imgs) {
				img.setRoomType(roomType);
				session.persist(img);
				dbImgs.add(img);
			}
			
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
