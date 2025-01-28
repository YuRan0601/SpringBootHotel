package com.cloudSerenityHotel.order.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.order.model.OrderItemsBean;

@Repository
public interface OrderItemsDao extends JpaRepository<OrderItemsBean, Integer> {

	/*
	 * 1.) 屬於 DAO（Data Access Object）層，直接操作資料庫，與資料表一一對應。
	 * 2.) 方法名稱如 findByOrderOrderId 是針對具體的資料庫查詢語法，利用 Spring Data JPA 的命名規則自動生成實現。
	 * 3.) 它主要的責任是「如何從資料庫拿資料」。
	 */
	
	// 根據訂單編號 (orderId) 查詢訂單細項
    List<OrderItemsBean> findByOrderOrderId(Integer orderId);
}
