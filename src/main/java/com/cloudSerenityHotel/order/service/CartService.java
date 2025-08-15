package com.cloudSerenityHotel.order.service;

import java.util.List;

import com.cloudSerenityHotel.order.dto.CartItemFrontendDTO;
import com.cloudSerenityHotel.order.dto.MemberForCartFrontendDTO;
import com.cloudSerenityHotel.order.model.Cart;
import com.cloudSerenityHotel.order.model.CartItems;

public interface CartService {
	
	// --- DTO 轉換 ---
    CartItemFrontendDTO convertToFrontendDTO(CartItems cartItem);
	
	 // --- 前台會員資訊 ---
    MemberForCartFrontendDTO getMemberForCart(int userId);

    // --- 購物車 CRUD ---
    Cart createNewCart(Integer userId);
    CartItems createNewCartItem(Cart cart, Integer productId, int quantity);
    CartItemFrontendDTO addToCart(Integer userId, Integer productId, int quantity);
    List<CartItemFrontendDTO> getCartItems(Integer userId);
    void removeFromCart(Integer userId, Integer productId);
    void clearCart(Integer userId);
    CartItemFrontendDTO updateCartItem(Integer userId, Integer productId, int newQuantity);
}