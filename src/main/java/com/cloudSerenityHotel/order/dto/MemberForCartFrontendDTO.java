package com.cloudSerenityHotel.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForCartFrontendDTO {
    private int userid;
    private String userName;
    private String email;
    private String userIdentity; // 管理員或會員身份
    private String phone;
    private String address; // 地址可能為空
    private String paymentMethod; // 新增付款方式
    
    // 新增一個帶有 6 個參數的構造函數
    public MemberForCartFrontendDTO(int userid, String userName, String email, String phone, String address, String paymentMethod) {
        this.userid = userid;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.paymentMethod = paymentMethod;
    }
}
