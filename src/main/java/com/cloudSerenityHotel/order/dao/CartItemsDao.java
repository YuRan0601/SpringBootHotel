package com.cloudSerenityHotel.order.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cloudSerenityHotel.order.model.CartItems;

@Repository
public interface CartItemsDao extends JpaRepository<CartItems, Integer>{

	// 查詢某個購物車的所有商品
    List<CartItems> findByCartCartId(Integer cartId);

    // 查詢購物車內某商品（避免重複加入）
    Optional<CartItems> findByCartCartIdAndProductsProductId(Integer cartId, Integer productId);
}
