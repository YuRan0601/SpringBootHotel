package com.cloudSerenityHotel.order.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.cloudSerenityHotel.order.dao.OrderItemsDao;
import com.cloudSerenityHotel.order.model.OrderItemsBean;
import com.cloudSerenityHotel.utils.HibernateUtil;

public class OrderItemsDaoImpl implements OrderItemsDao {

	// 去拿 OpenSessionViewFilter 寫好的 SessionFactory()方法
	private SessionFactory factory;

	public OrderItemsDaoImpl() {
		this.factory = HibernateUtil.getSessionFactory();
	}

	@Override
	public OrderItemsBean insert(OrderItemsBean orderItems) {
		// 不用開交易,交給 OpenSessionViewFilter 去做 beginTransaction()
		Session session = factory.getCurrentSession();
		// 確保只進行 persist，而不是 find
	    session.persist(orderItems);
	    // 輸出確認 orderId 是否正確
	    //System.out.println("Inserted OrderItems with OrderId: " + orderItems.getOrderId());
	    return orderItems; // 返回插入後的 orderItems 物件
	}

	@Override
	public OrderItemsBean selectById(Integer orderitemId) {
		// 不用開交易,交給 OpenSessionViewFilter 去做 beginTransaction()
		Session session = factory.getCurrentSession();
		String hql = "from OrderItemsBean oib where oib.orderitemId= :oii";
		Query<OrderItemsBean> query = session.createQuery(hql,OrderItemsBean.class);
		// 設定查詢條件的參數
		query.setParameter("oii", orderitemId);
		// 返回唯一的結果
		return query.uniqueResult();
	}

	@Override
	public List<OrderItemsBean> selectAll() {
		// 不用開交易,交給 OpenSessionViewFilter 去做 beginTransaction()
		Session session = factory.getCurrentSession();
		Query<OrderItemsBean> querySelectAll = session.createQuery("from OrderItemsBean", OrderItemsBean.class);
		return querySelectAll.list();
	}

	// 暫且不改訂單明細(如有誤應該整單刪除-勿複雜商業邏輯)
	/*
	 * @Override public OrderItemsBean updateOne(OrderItemsBean orderItems) { return
	 * null; }
	 */

	@Override
	public boolean deleteOne(Integer orderitemId) {
		// 不用開交易,交給 OpenSessionViewFilter 去做 beginTransaction()
		Session session = factory.getCurrentSession();
		OrderItemsBean orderItemsBean = session.find(OrderItemsBean.class, orderitemId);
		if (orderItemsBean != null) {
			session.remove(orderItemsBean);
			return true;
		}
		return false;
	}

	@Override
	public List<OrderItemsBean> selectOrderItemsByOrderId(Integer orderId) {
		// 不用開交易,交給 OpenSessionViewFilter 去做 beginTransaction()
		Session session = factory.getCurrentSession();
		// 使用 HQL 查詢訂單細項
		String hql = "from OrderItemsBean oi where oi.orderId= :oid";
		Query<OrderItemsBean> query = session.createQuery(hql, OrderItemsBean.class);
		query.setParameter("oid", orderId);
		// 返回一個 List<OrderItemsBean>
		return query.list();
	}

}
