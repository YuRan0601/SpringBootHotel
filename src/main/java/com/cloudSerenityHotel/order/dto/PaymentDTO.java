package com.cloudSerenityHotel.order.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
	private Integer orderId;       // 訂單 ID
    private BigDecimal finalAmount;     // 最終金額
    private String productName;       // 商品名稱
    private String paymentMethod;  // 付款方式（例如：信用卡）
}
