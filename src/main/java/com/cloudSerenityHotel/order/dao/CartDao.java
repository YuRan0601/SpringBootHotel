package com.cloudSerenityHotel.order.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cloudSerenityHotel.order.model.Cart;

@Repository
public interface CartDao extends JpaRepository<Cart, Integer>{
	// 查詢用戶的購物車
    Optional<Cart> findByUserId(Integer userId);
}
