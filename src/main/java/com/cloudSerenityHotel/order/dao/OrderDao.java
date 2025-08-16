package com.cloudSerenityHotel.order.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.cloudSerenityHotel.order.model.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order>{

	// 根據 UserID 查詢訂單
    List<Order> findByUserId(Integer userId);
    
 	// 根據 UserID 和 訂單 ID 查詢訂單
    Order findByUserIdAndOrderId(Integer userId, Integer orderId);
    
    // 根據 UserID 和 訂單狀態 查詢訂單
    List<Order> findByUserIdAndOrderStatus(Integer userId, String status);
    
    // 根據 訂單狀態 查詢訂單_可用於匯出訂單
    List<Order> findByOrderStatus(String status);
    
    // 根據付款方式（信用卡）和訂單狀態(未付款)查詢未付款訂單
    List<Order> findByPaymentMethodAndOrderStatus(String paymentMethod, String orderStatus);
}
