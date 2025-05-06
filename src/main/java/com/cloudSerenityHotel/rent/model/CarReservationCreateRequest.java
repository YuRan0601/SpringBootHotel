package com.cloudSerenityHotel.rent.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarReservationCreateRequest {

	private CarUserInfo carUserInfo;

	private String carId;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime rentalStart;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime rentalEnd;
}
