package com.cloudSerenityHotel.rent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CarReservationStatuEnum {

	RENTED("租借中"), RESERVED("已預約"), AVAILABLE("可租用");

	private String description;
}
