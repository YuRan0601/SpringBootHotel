package com.cloudSerenityHotel.rent.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.cloudSerenityHotel.rent.model.CarModelBean;
import com.cloudSerenityHotel.utils.HibernateUtil;

public class ModelDao {

	private SessionFactory factory;
	private Session session;

	public ModelDao() {
		this.factory = HibernateUtil.getSessionFactory();
	}

	// 新增
	public CarModelBean insertModel(CarModelBean modelBean) {
		this.session = factory.getCurrentSession();
		session.persist(modelBean);
		return modelBean;
	}

	// 修改
	public CarModelBean updateOne(Integer id, String newCarType) {
		this.session = factory.getCurrentSession();
		CarModelBean carModelBean = session.find(CarModelBean.class, id);
		if (carModelBean != null) {
			carModelBean.setCarType(newCarType);
			session.persist(carModelBean);
			return carModelBean;
		}
		return null;
	}

	// 修改
	public CarModelBean updateTime(Integer id) {
		this.session = factory.getCurrentSession();
		CarModelBean carModelBean = session.find(CarModelBean.class, id);
		if (carModelBean != null) {
			LocalDateTime localDateTime = LocalDateTime.now();
			Timestamp timestamp = Timestamp.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
			carModelBean.setUpdatedAt(timestamp);
			return carModelBean;
		}
		return null;
	}

	public CarModelBean updateDescriptionAndSizeByCarId(Integer carId, String description, String carSize,
			Timestamp updateTime) {
		this.session = factory.getCurrentSession();
		CarModelBean carModelBean = session.find(CarModelBean.class, carId);
		if (carModelBean != null) {
			carModelBean.setDescription(description);
			carModelBean.setCarSize(carSize);
			carModelBean.setUpdatedAt(updateTime);
			return carModelBean;
		}

		return null;
	}

	// 刪除
	public boolean deleteOne(Integer id) {
		this.session = factory.getCurrentSession();
		CarModelBean modelBean = session.find(CarModelBean.class, id);
		if (modelBean != null) {
			session.remove(modelBean);
			return true;
		}
		return false;
	}

	// 查詢(單)
	public CarModelBean selectById(Integer id) {
		this.session = factory.getCurrentSession();
		return session.find(CarModelBean.class, id);
	}

	// 查詢(全)
	public List<CarModelBean> selectAll() {
		this.session = factory.getCurrentSession();
		Query<CarModelBean> query = session.createQuery("from CarModelBean", CarModelBean.class);
		return query.list();
	}

	// 查詢(模糊)
	public List<CarModelBean> selectByCarBrand(String brand) {
		this.session = factory.getCurrentSession();
		String hql = "FROM CarModelBean c WHERE c.brand LIKE :brand";
		Query<CarModelBean> query = session.createQuery(hql, CarModelBean.class);
		query.setParameter("brand", "%" + brand + "%"); // 使用 % 作為通配符
		return query.list();
	}
}
