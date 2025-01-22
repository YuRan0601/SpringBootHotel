package com.cloudSerenityHotel.order.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderDTO {
	private Integer orderId; // 訂單唯一編號
	private Integer userId; // 使用者編號
	private String receiveName; // 收件人名稱
	private String email; // 電子信箱
	private String phoneNumber; // 電話號碼
	private String address; // 地址
	private String orderStatus; // 訂單狀態
	private String paymentMethod; // 付款方式
	//將日期類型（Timestamp）轉為字串，方便前端處理。
	private String totalAmount; // 格式化的訂單總金額
	private int pointsDiscount; // 點數折抵(不確定的功能 先保留)
    private String discountAmount; // 格式化的折扣金額
    private String finalAmount; // 格式化的最終金額
    private String orderDate; // 格式化的訂單建立日期
    private String updatedAt; // 格式化的更新日期
	
	// 訂單細項列表 
	private List<OrderItemDTO> orderItemsDtos;
}
