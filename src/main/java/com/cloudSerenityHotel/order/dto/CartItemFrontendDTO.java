package com.cloudSerenityHotel.order.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItemFrontendDTO {
	private String productName; // 商品名稱
    private String imageUrl; // 商品圖片
    private int quantity; // 商品數量
    private BigDecimal unitPrice; // 單價
    private BigDecimal discount; // 折扣金額
    private BigDecimal subtotal; // 小計
    
    public CartItemFrontendDTO(String productName, String imageUrl, int quantity, BigDecimal unitPrice, BigDecimal discount, BigDecimal subtotal) {
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.subtotal = subtotal;
    }
}
