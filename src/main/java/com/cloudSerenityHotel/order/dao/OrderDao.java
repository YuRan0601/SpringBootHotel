package com.cloudSerenityHotel.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.order.model.OrderBean;

@Repository
public interface OrderDao extends JpaRepository<OrderBean, Integer>{

	// 新
	//OrderBean insert(OrderBean order);

	// 查一
	//OrderBean selectById(Integer orderId);

	// 查全
	//List<OrderBean> selectAll();

	// 改
	//OrderBean updateOne(OrderBean order);

	// 刪
	//boolean deleteOne(Integer orderId);
}
