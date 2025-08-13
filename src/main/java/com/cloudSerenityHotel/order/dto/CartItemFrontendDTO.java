package com.cloudSerenityHotel.order.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItemFrontendDTO {
	private int cartItemId;  // 確保有 cartItemId
	private int productId;  // 添加 productId
	private String productName; // 商品名稱
    private String imageUrl; // 商品圖片
    private int quantity; // 商品數量
    private BigDecimal unitPrice; // 單價
    private BigDecimal discount; // 折扣金額
    private BigDecimal subtotal; // 小計
    
}