package com.cloudSerenityHotel.order.service;

import java.util.List;
import com.cloudSerenityHotel.order.dto.CartTurntoOrderDTO;
import com.cloudSerenityHotel.order.dto.OrderBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderFrontendDTO;
import com.cloudSerenityHotel.order.model.Order;
import com.cloudSerenityHotel.order.model.OrderItems;

public interface OrderService {

	// --- 前台 / 後台 DTO 轉換 ---
	/*
	 * 目前的 Service Implementation 返回的是實體類（例如 OrderBean、OrderItemsBean），需要在返回之前轉換為
	 * DTO（OrderDTO、OrderItemDTO）。
	 */
    OrderBackendDTO convertToBackendDTO(Order order);
    OrderFrontendDTO convertToFrontendDTO(Order order);

    // --- 查詢 ---
    List<OrderBackendDTO> findAllOrders();
    OrderBackendDTO getOrderDetailsAsDTO(Integer orderId);
    List<OrderFrontendDTO> getOrdersForFrontendByUserId(Integer userId);
    List<OrderFrontendDTO> getOrdersByUserIdAndStatus(Integer userId, String status);

    // --- CRUD ---
    OrderBackendDTO insertOrderWithItems(Order order, List<OrderItems> items);
    OrderBackendDTO updateOrder(Integer orderId, Order updatedOrder);
    boolean deleteOrderById(Integer orderId);

    // --- 業務邏輯 ---
    OrderBackendDTO createOrder(CartTurntoOrderDTO orderRequest);
    void calculateOrderTotal(Order order, List<OrderItems> items);
    void paymentSuccess(Integer orderId);
}
