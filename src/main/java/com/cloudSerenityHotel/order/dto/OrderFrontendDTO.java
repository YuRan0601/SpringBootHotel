package com.cloudSerenityHotel.order.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderFrontendDTO {
	private Integer orderId;         // 訂單編號
    private String receiveName;      // 收件人名稱
    private String email;            // 收件人電子信箱
    private String phoneNumber;      // 收件人電話
    private String address;          // 收件人地址
    private String orderStatus;      // 訂單狀態
    private String paymentMethod;    // 付款方式
    private String totalAmount;      // 總金額（格式化）
    private String finalAmount;      // 最終金額（格式化）
    private String orderDate;        // 訂單日期（格式化）
    
    // 訂單細項列表
    private List<OrderItemFrontendDTO> orderItemsDtos;
}
