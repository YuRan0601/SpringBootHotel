package com.cloudSerenityHotel.rent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarModelImage {

	private int carId; // 編號
	private String carModel; // 車型名稱
	private String brand; // 品牌
	private String image;

}
