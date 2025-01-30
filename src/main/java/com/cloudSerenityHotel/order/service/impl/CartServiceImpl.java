package com.cloudSerenityHotel.order.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloudSerenityHotel.order.dao.CartDao;
import com.cloudSerenityHotel.order.dao.CartItemsDao;
import com.cloudSerenityHotel.order.dto.CartItemFrontendDTO;
import com.cloudSerenityHotel.order.model.Cart;
import com.cloudSerenityHotel.order.model.CartItems;
import com.cloudSerenityHotel.order.service.CartService;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;

import jakarta.transaction.Transactional;

//JPA 的 save 方法會根據物件是否有主鍵來自動選擇是執行「新增」還是「更新」。
@Service
@Transactional // 自動交易管理員
public class CartServiceImpl implements CartService{

	@Autowired
	private CartDao cartDao;
	
	@Autowired
	private CartItemsDao cartItemsDao;
	
	@Autowired
	private ProductServiceImpl productService;
	
	// 將 CartItems 實體轉換為前台 DTO
    public CartItemFrontendDTO convertToFrontendDTO(CartItems cartItem) {
    	 // 獲取商品圖片列表
        List<ProductImages> productImages = cartItem.getProducts().getProductImages();

        // 判斷列表是否為空，若非空則取第一張圖片，否則設為默認圖片或空字串
        String imageUrl = productImages != null && !productImages.isEmpty()
                ? productImages.get(0).getImageUrl() // 獲取第一張圖片的 URL
                : ""; // 默認值，當無圖片時

        return new CartItemFrontendDTO(
                cartItem.getProducts().getProductName(), // 商品名稱
                imageUrl, // 商品圖片 URL
                cartItem.getQuantity(),
                cartItem.getUnitPrice(),
                cartItem.getDiscount(),
                cartItem.getSubtotal()
            );
    }
	
    // 加入商品到購物車
	@Override
	public void addToCart(Integer userId, Integer productId, int quantity) {
		// 查詢用戶的購物車，如果不存在則創建新購物車
	    Cart cart = cartDao.findByUserId(userId).orElseGet(() -> createNewCart(userId));

	    // 查詢商品，確保商品存在且未下架
	    Products product = productService.findById(productId)
	            .orElseThrow(() -> new RuntimeException("商品不存在"));

	    if (product.getStatus() == 0) {
	        throw new RuntimeException("該商品已下架，無法加入購物車");
	    }

	    // 查詢購物車內是否已經有此商品
	    Optional<CartItems> existingItem = cartItemsDao.findByCartCartIdAndProductsProductId(cart.getCartId(), productId);

	    if (existingItem.isPresent()) {
	        // 如果商品已存在，增加數量並更新小計
	        CartItems cartItem = existingItem.get();
	        cartItem.setQuantity(cartItem.getQuantity() + quantity);
	        cartItem.setSubtotal(cartItem.getUnitPrice().subtract(cartItem.getDiscount())
	                .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
	        cartItemsDao.save(cartItem);
	    } else {
	        // 如果商品不存在於購物車，新增一筆購物車紀錄
	        BigDecimal unitPrice = product.getSpecialPrice() != null ? product.getSpecialPrice() : product.getPrice();
	        BigDecimal discount = product.getSpecialPrice() != null ? product.getPrice().subtract(product.getSpecialPrice()) : BigDecimal.ZERO;
	        BigDecimal subtotal = unitPrice.subtract(discount).multiply(BigDecimal.valueOf(quantity));

	        CartItems newCartItem = new CartItems();
	        newCartItem.setCart(cart);
	        newCartItem.setProducts(product); // 設置完整的商品資訊
	        newCartItem.setQuantity(quantity);
	        newCartItem.setUnitPrice(unitPrice);
	        newCartItem.setDiscount(discount);
	        newCartItem.setSubtotal(subtotal);
	        newCartItem.setIsValid(0); // 默認為有效
	        cartItemsDao.save(newCartItem);
	    }
	}
	
	// 創建新的購物車
    private Cart createNewCart(Integer userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartDao.save(cart);
    }

	// 查詢購物車內容
	@Override
	public List<CartItemFrontendDTO> getCartItems(Integer userId) {
		// 查詢購物車
        Cart cart = cartDao.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("購物車不存在"));

        // 查詢購物車內所有商品
        List<CartItems> cartItems = cartItemsDao.findByCartCartId(cart.getCartId());

        // 檢查商品狀態並過濾無效商品
        return cartItems.stream()
                .peek(item -> {
                    if (item.getProducts().getStatus() == 0) {
                        item.setIsValid(1); // 商品下架
                        cartItemsDao.save(item);
                    } else if (!item.getUnitPrice().equals(item.getProducts().getSpecialPrice())) {
                        item.setIsValid(2); // 價格變動
                        cartItemsDao.save(item);
                    }
                })
                .filter(item -> item.getIsValid() == 0) // 過濾有效商品
                .map(this::convertToFrontendDTO) // 轉換為前台 DTO
                .collect(Collectors.toList());
	}

	// 移除購物車中的某個商品
	@Override
	public void removeFromCart(Integer userId, Integer productId) {
		// 查詢購物車
        Cart cart = cartDao.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("購物車不存在"));

        // 查詢商品是否在購物車內
        CartItems cartItem = cartItemsDao.findByCartCartIdAndProductsProductId(cart.getCartId(), productId)
                .orElseThrow(() -> new RuntimeException("商品不存在於購物車中"));

        // 刪除商品
        cartItemsDao.delete(cartItem);
		
	}

	// 要再問!
	// 修改購物車中的商品數量
    @Override
    public void updateCartItem(Integer userId, Integer productId, int newQuantity) {
        Cart cart = cartDao.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("購物車不存在"));
        CartItems cartItem = cartItemsDao.findByCartCartIdAndProductsProductId(cart.getCartId(), productId)
                .orElseThrow(() -> new RuntimeException("商品不存在於購物車中"));
        if (newQuantity <= 0) {
            cartItemsDao.delete(cartItem);
        } else {
            cartItem.setQuantity(newQuantity);
            cartItem.setSubtotal(cartItem.getUnitPrice().subtract(cartItem.getDiscount())
                    .multiply(BigDecimal.valueOf(newQuantity)));
            cartItemsDao.save(cartItem);
        }
    }

    // 清空購物車
    @Override
    public void clearCart(Integer userId) {
        Cart cart = cartDao.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("購物車不存在"));
        List<CartItems> cartItems = cartItemsDao.findByCartCartId(cart.getCartId());
        cartItemsDao.deleteAll(cartItems);
    }

}
