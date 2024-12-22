package com.cloudSerenityHotel.order.dao;

import java.util.List;
import com.cloudSerenityHotel.order.model.OrderItemsBean;

public interface OrderItemsDao {
	// 新
	OrderItemsBean insert(OrderItemsBean orderItems);

	// 查一
	OrderItemsBean selectById(Integer orderitemId);

	// 查全
	List<OrderItemsBean> selectAll();

	// 改
	//OrderItemsBean updateOne(OrderItemsBean orderItems);

	// 刪
	boolean deleteOne(Integer orderitemId);
	
	// 依據訂單Id,查詢訂單下的訂單細項
	List<OrderItemsBean> selectOrderItemsByOrderId(Integer orderId);
}
