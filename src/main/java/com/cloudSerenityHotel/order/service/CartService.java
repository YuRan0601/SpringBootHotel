package com.cloudSerenityHotel.order.service;

import java.util.List;

import com.cloudSerenityHotel.order.dto.CartItemFrontendDTO;
import com.cloudSerenityHotel.order.model.CartItems;

public interface CartService {
	// 將 CartItems 實體轉換為前台 DTO
	CartItemFrontendDTO convertToFrontendDTO(CartItems cartItem);
	
	// 加入商品到購物車
    void addToCart(Integer userId, Integer productId, int quantity);

    // 查詢購物車內容
    List<CartItemFrontendDTO> getCartItems(Integer userId);
    //List<CartItemDTO> getCartItems(Integer userId);

    // 刪除購物車內的某個商品
    void removeFromCart(Integer userId, Integer productId);

    // 清空購物車
    void clearCart(Integer userId);

    // 修改購物車中的商品數量並返回更新後的 DTO
    CartItemFrontendDTO updateCartItem(Integer userId, Integer productId, int newQuantity);

}
