package com.cloudSerenityHotel.order.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDTO {
	private Integer orderitemId; // 關聯唯一編號，自動增長
	private Integer orderId; // 所屬訂單ID
	private Integer productId; // 購買商品的ID
	private String productName; // 購買商品的名稱（對應 Products 的 name）
	private BigDecimal productPrice; // 商品單價（來自 Products 表的 price 字段）
	private Integer quantity; // 購買數量
	private BigDecimal unitPrice; // 單價
	private BigDecimal discount; // 單項商品的折扣金額
	private BigDecimal subtotal; // 小計（(UnitPrice - Discount) * Quantity）s
}
