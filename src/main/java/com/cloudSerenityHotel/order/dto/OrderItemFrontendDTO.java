package com.cloudSerenityHotel.order.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemFrontendDTO {
	private int orderitemId; // 關聯唯一編號，自動增長
	private int productId; // 購買商品的ID
    private String productName;      // 商品名稱
    private int quantity;        // 購買數量
    private BigDecimal unitPrice;    // 單價
    private BigDecimal specialPrice; // 特別價格
    private BigDecimal discount;     // 折扣金額
    private BigDecimal subtotal;     // 小計
}
