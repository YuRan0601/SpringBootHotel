package com.cloudSerenityHotel.rent.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.cloudSerenityHotel.rent.model.ImagesBean;
import com.cloudSerenityHotel.utils.HibernateUtil;

public class ImagesDao2 {

	private SessionFactory factory;
	private Session session;

	public ImagesDao2() {
		this.factory = HibernateUtil.getSessionFactory();
	}

	// 新增
	public ImagesBean insert(ImagesBean imageBean) {
		session = factory.getCurrentSession();
		session.persist(imageBean);
		return imageBean;
	}

	public ImagesBean selectCarPrimary(Integer carId, Integer isPrimary) {
		session = factory.getCurrentSession();

		String hql = "from ImagesBean i WHERE i.carId=:carId and i.isPrimary=:isPrimary";
		Query<ImagesBean> query = session.createQuery(hql, ImagesBean.class);
		query.setParameter("carId", carId);
		query.setParameter("isPrimary", isPrimary);
		List<ImagesBean> carIsPrimary = query.getResultList();

		if (!carIsPrimary.isEmpty()) {
			System.out.println(carIsPrimary.get(0).getCarId() + " : " + carIsPrimary.get(0).getIsPrimary());
			return carIsPrimary.get(0);
		}

		return null;
	}

	public List<ImagesBean> selectAllById(Integer carid) {
		session = factory.getCurrentSession();
		Query<ImagesBean> query = session.createQuery("from ImagesBean", ImagesBean.class);
		return query.list();
	}

	public ImagesBean updateOne(Integer id, String newImageUrl) {
		session = factory.getCurrentSession();
		ImagesBean imagesBean = session.find(ImagesBean.class, id);
		if (imagesBean != null) {
			imagesBean.setImageUrl(newImageUrl);
			session.persist(imagesBean);
			return imagesBean;
		}
		return null;
	}

	public boolean deleteOne(Integer id) {
		session = factory.getCurrentSession();
		ImagesBean imagesBean = session.find(ImagesBean.class, id);
		if (imagesBean != null) {
			session.remove(imagesBean);
			return true;
		}
		return false;
	}

}
