package com.cloudSerenityHotel.order.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.cloudSerenityHotel.order.dao.OrderDao;
import com.cloudSerenityHotel.order.model.OrderBean;
import com.cloudSerenityHotel.utils.HibernateUtil;

public class OrderDaoImpl implements OrderDao {

	// 去拿 OpenSessionViewFilter 寫好的 SessionFactory()方法
	private SessionFactory factory;

	public OrderDaoImpl() {
		this.factory = HibernateUtil.getSessionFactory();
	}

	// 新
	@Override
	public OrderBean insert(OrderBean orderBean) {
		// 不用開交易,交給 OpenSessionViewFilter 去做 beginTransaction()
		Session session = factory.getCurrentSession();
		// 直接將 orderBean 持久化，避免查詢
		session.persist(orderBean);
		return orderBean;
	}

	// 查一
	@Override
	public OrderBean selectById(Integer orderId) {
		// 不用開交易,交給 OpenSessionViewFilter 去做 beginTransaction()
		Session session = factory.getCurrentSession();
		String hql = "from OrderBean ob where ob.orderId= :oi";
		Query<OrderBean> query = session.createQuery(hql, OrderBean.class);
		// 設定查詢條件的參數
		query.setParameter("oi", orderId);
		// 返回唯一的結果
		return query.uniqueResult();
	}

	// 查全
	@Override
	public List<OrderBean> selectAll() {
		// 不用開交易,交給 OpenSessionViewFilter 去做 beginTransaction()
		Session session = factory.getCurrentSession();
		Query<OrderBean> querySelectAll = session.createQuery("from OrderBean", OrderBean.class);
		return querySelectAll.list();
	}

	// 改
	@Override
	public OrderBean updateOne(OrderBean order) {
		// 不用開交易,交給 OpenSessionViewFilter 去做 beginTransaction()
		Session session = factory.getCurrentSession();
		// 查詢需要更新的實體，保證其處於持久化狀態
		OrderBean orderBean = session.find(OrderBean.class, order.getOrderId());
		if (orderBean != null) {
			// 不更新 orderId, 只更新其他屬性
			// orderBean.setOrderId(order.getOrderId());
			orderBean.setUserId(order.getUserId());
			orderBean.setReceiveName(order.getReceiveName());
			orderBean.setEmail(order.getEmail());
			orderBean.setPhoneNumber(order.getPhoneNumber());
			orderBean.setAddress(order.getAddress());
			orderBean.setOrderStatus(order.getOrderStatus());
			orderBean.setPaymentMethod(order.getPaymentMethod());
			orderBean.setTotalAmount(order.getTotalAmount());
			orderBean.setPointsDiscount(order.getPointsDiscount());
			orderBean.setDiscountAmount(order.getDiscountAmount());
			orderBean.setFinalAmount(order.getFinalAmount());
			orderBean.setUpdatedAt(new Timestamp(System.currentTimeMillis())); // 更新時間

			// Hibernate 自動同步到資料庫，無需額外調用 session.update()
			// 回傳更新後的實體
			return orderBean;
		} else {
			// 找不到該訂單時，拋出異常
			throw new IllegalArgumentException("未找到對應的訂單，無法更新");
		}
	}

	// 刪
	@Override
	public boolean deleteOne(Integer orderId) {
		// 不用開交易,交給 OpenSessionViewFilter 去做 beginTransaction()
		Session session = factory.getCurrentSession();
		OrderBean orderBean = session.find(OrderBean.class, orderId);
		if (orderBean != null) {
			session.remove(orderBean);
			return true;
		}
		return false;
	}

}
