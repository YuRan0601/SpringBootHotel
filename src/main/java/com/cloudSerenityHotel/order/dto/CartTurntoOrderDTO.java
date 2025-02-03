package com.cloudSerenityHotel.order.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartTurntoOrderDTO {
    private List<CartItemFrontendDTO> orderItems;  // 訂單項目
    private MemberForCartFrontendDTO recipient;    // 收件人資料
    private BigDecimal totalAmount;                // 訂單總金額
}
