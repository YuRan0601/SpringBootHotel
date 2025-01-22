package com.cloudSerenityHotel.order.service;

import java.util.List;

import com.cloudSerenityHotel.order.dto.OrderDTO;
import com.cloudSerenityHotel.order.model.OrderBean;
import com.cloudSerenityHotel.order.model.OrderItemsBean;

public interface OrderService {
	OrderDTO getOrderDetailsAsDTO(Integer orderId);

	// DTO 的轉換功能
	/*
	 * 目前的 Service Implementation 返回的是實體類（例如 OrderBean、OrderItemsBean），需要在返回之前轉換為
	 * DTO（OrderDTO、OrderItemDTO）。
	 */
	OrderDTO convertToDTO(OrderBean order);

	// 整筆訂單
	boolean deleteOrderById(Integer orderId); // 刪除訂單
	List<OrderDTO> findAllOrders(); // 查詢所有訂單，返回 DTO 列表
	OrderDTO findOrderDetails(Integer orderId); // 查詢單筆訂單（包括細項）

	// 單純訂單細項
	List<OrderItemsBean> insertItemsToExistingOrder(Integer orderId, List<OrderItemsBean> items); // 為已存在訂單新增訂單細項
	OrderItemsBean updateOrderItem(OrderItemsBean orderItem); // 修改單一訂單細項
	boolean deleteOrderItemById(Integer orderItemId); // 刪除單一訂單細項

	// 訂單 & 訂單細項
	OrderDTO insertOrderWithItems(OrderBean orderBean, List<OrderItemsBean> items);
	void updateOrderItems(int orderId, List<OrderItemsBean> orderItems);
	//OrderDTO updateOrderById(Integer orderId, OrderDTO updatedOrderDTO);
		//將 OrderDTO 轉換為 OrderBean
		//OrderBean convertToEntity(OrderDTO orderDTO);

	// 較複雜邏輯運算
	void calculateOrderTotal(OrderBean order, List<OrderItemsBean> items);



}
