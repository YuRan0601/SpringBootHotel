package com.cloudSerenityHotel.order.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.order.model.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer>{
	// 分頁查詢所有訂單_未使用
	Page<Order> findAll(Pageable pageable);
	
	// 根據 userId 查詢訂單
    List<Order> findByUserId(Integer userId);
    
 	// 根據用戶 ID 和訂單 ID 查詢訂單
    Order findByUserIdAndOrderId(Integer userId, Integer orderId);
    
    // 根據 付款方式 查詢訂單_可能不會用到
    List<Order> findByOrderStatus(String paymentMethod);
    
    // 根據付款方式（信用卡）和訂單狀態查詢未付款訂單
    List<Order> findByPaymentMethodAndOrderStatus(String paymentMethod, String orderStatus);
}
