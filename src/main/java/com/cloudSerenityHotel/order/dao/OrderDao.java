package com.cloudSerenityHotel.order.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.order.model.OrderBean;

@Repository
public interface OrderDao extends JpaRepository<OrderBean, Integer>{
	// 分頁查詢所有訂單_未使用
	Page<OrderBean> findAll(Pageable pageable);
	
	// 根據 userId 查詢訂單
    List<OrderBean> findByUserId(Integer userId);
}
