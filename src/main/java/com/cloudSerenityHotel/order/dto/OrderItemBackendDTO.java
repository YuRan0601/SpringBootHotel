package com.cloudSerenityHotel.order.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemBackendDTO {
	private Integer orderitemId; // 訂單細項
	private Integer orderId; // 所屬訂單ID
	private Integer productId; // 購買商品的ID
	private String productName; // 購買商品的名稱（對應 Products 的 name）
	private String productMainImage; // 商品主照片 URL
	private BigDecimal productPrice; // 商品單價（來自 Products 表的 price 字段）
	private BigDecimal specialPrice;  // 商品特價（來自 Products 表的 specialPrice 如果有的話）
	private Integer quantity; // 購買數量
	private BigDecimal unitPrice; // 單價(原價)
	private BigDecimal discount; // 單項商品的折扣金額(原價-特價)
	private BigDecimal subtotal; // 小計（(UnitPrice - Discount) * Quantity）
}