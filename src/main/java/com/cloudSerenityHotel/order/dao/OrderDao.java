package com.cloudSerenityHotel.order.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudSerenityHotel.order.model.OrderBean;

@Repository
public interface OrderDao extends JpaRepository<OrderBean, Integer>{
	Page<OrderBean> findAll(Pageable pageable);
}
