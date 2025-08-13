package com.cloudSerenityHotel.order.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * 訂單細項金額（後端可能還要計算 subtotal、折扣） → BigDecimal
 */
@Getter @Setter
public class OrderItemFrontendDTO {
	private int orderitemId; // 訂單細項
	private int productId; // 購買商品的ID
    private String productName;      // 商品名稱
    private String productMainImage; // 商品主照片 URL
    private int quantity;        // 購買數量
    private BigDecimal unitPrice;    // 單價
    private BigDecimal specialPrice; // 特別價格
    private BigDecimal discount;     // 折扣金額
    private BigDecimal subtotal;     // 小計
}