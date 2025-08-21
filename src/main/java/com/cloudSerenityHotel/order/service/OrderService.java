package com.cloudSerenityHotel.order.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    /** 根據 orderId 查詢 Order 實體 */
    Optional<Order> findById(Integer orderId);
    List<OrderFrontendDTO> getOrdersForFrontendByUserId(Integer userId);
    OrderFrontendDTO getOrderDetailForFrontend(Integer userId, Integer orderId);
    List<OrderFrontendDTO> getOrdersByUserIdAndStatus(Integer userId, String status);
    // 過渡 / legacy
    List<OrderBackendDTO> getOrdersByStatus(String status);
    List<OrderBackendDTO> findAllOrders();
    OrderBackendDTO getOrderDetailsAsDTO(Integer orderId);
    // 條件組合查詢
    List<OrderBackendDTO> findOrders(
	        Integer orderId,
	        Integer userId,
	        LocalDate startDate,
	        LocalDate endDate,
	        String paymentMethod,
	        List<String> orderStatuses); // 改成 List<String>

    // --- CRUD ---
    OrderBackendDTO insertOrderWithItems(Order order, List<OrderItems> items);
    OrderBackendDTO updateOrder(Integer orderId, Order updatedOrder);
    OrderBackendDTO voidOrder(Integer orderId); // 假刪除(作廢)
    boolean deleteOrderById(Integer orderId);

    // --- 業務邏輯 ---
    Order createOrderEntity(CartTurntoOrderDTO orderRequest);
    void calculateOrderTotal(Order order, List<OrderItems> items);
    void paymentSuccess(Integer orderId);
}
