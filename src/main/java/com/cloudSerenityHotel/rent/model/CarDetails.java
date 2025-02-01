package com.cloudSerenityHotel.rent.model;

import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDetails {
	
	@Id
	@Column(name = "car_id")
	private String carId; //汽車ID
	
	@Column(name = "carmodel_id")
	private int carModelId; //車型ID
	
	@Column(name = "license_plate")
	private String licensePlate; //車牌號碼
	
	@Column(name = "color_options")
	private String colorOptions; //顏色選項
	
	@Column(name = "year")
	private int year; //汽車年份
	
	@Column(name = "status")
	private String status; //車輛狀態
	
	@Column(name = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt; //創建時間
	
	@Column(name = "updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime updatedAt; //更新時間
	
	@Column(name = "remarks")
	private String remarks; //備註
	
}
