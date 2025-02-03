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
}
