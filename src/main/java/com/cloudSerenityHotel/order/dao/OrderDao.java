package com.cloudSerenityHotel.order.dao;

import java.util.List;
import com.cloudSerenityHotel.order.model.OrderBean;


public interface OrderDao {
		// 新
		OrderBean insert(OrderBean order);
		// 查一
		OrderBean selectById(Integer orderId);
		// 查全
		List<OrderBean> selectAll();
		// 改
		OrderBean updateOne(OrderBean order);
		// 刪
		boolean deleteOne(Integer orderId);
}	
