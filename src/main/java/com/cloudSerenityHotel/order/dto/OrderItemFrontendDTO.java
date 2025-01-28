package com.cloudSerenityHotel.order.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemFrontendDTO {
	private String productName;      // 商品名稱
    private Integer quantity;        // 購買數量
    private BigDecimal unitPrice;    // 單價
    private BigDecimal discount;     // 折扣金額
    private BigDecimal subtotal;     // 小計
}
