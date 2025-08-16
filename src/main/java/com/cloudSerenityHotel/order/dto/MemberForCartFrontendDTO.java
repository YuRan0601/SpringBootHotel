package com.cloudSerenityHotel.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForCartFrontendDTO {
    private int userid;
    private String receiveName; // 收件人名稱
    private String userName;    // 會員姓名
    private String email;
    private String phone;
    private String address; // 地址可能為空
    private String paymentMethod; // 新增付款方式
  
}