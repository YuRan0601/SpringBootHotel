package com.cloudSerenityHotel.booking.vo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EcpayOrderVo {
	private Integer orderId;
	private String productName;
	private BigDecimal price;
}
