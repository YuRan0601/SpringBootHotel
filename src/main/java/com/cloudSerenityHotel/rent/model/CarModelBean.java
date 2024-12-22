package com.cloudSerenityHotel.rent.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carmodel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarModelBean {

	@Id
	@Column(name = "car_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer carId; // 編號

	@Column(name = "car_model")
	private String carModel; // 車型名稱

	@Column(name = "description")
	private String description; // 描述

	@Column(name = "brand")
	private String brand; // 品牌

	@Column(name = "fuel_efficiency")
	private BigDecimal fuelEfficiency; // 油耗

	@Column(name = "seating_capacity")
	private Integer seatingCapacity; // 乘載人數

	@Column(name = "total_vehicles")
	private Integer totalVehicles; // 車輛總數

	@Column(name = "available_vehicles")
	private Integer availableVehicles; // 可用數量

	@Column(name = "created_at")
	private Timestamp createdAt; // 創建時間

	@Column(name = "updated_at")
	private Timestamp updatedAt; // 更新時間

	@Column(name = "car_type")
	private String carType; // 車輛類型

	@Column(name = "car_size")
	private String carSize; // 車型尺寸
}
