package com.cloudSerenityHotel.rent.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "cars_model")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarModel {
	
	@Id 
	@Column(name = "carmodel_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int carModelId; 
	
	@Column(name = "car_model")
	private String carModel; //車輛型號
	
	@Column(name = "description")
	private String description; //描述
	
	@Column(name = "brand")
	private String brand;	//品牌
	
	@Column(name = "engine_displacement")
	private int engineDisplacement; //排氣量
	
	@Column(name = "seating_capacity")
	private int seatingCapacity;	//座位數量
	
	@Column(name = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createdAt;		//創建時間
	
	@Column(name = "updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updatedAt;		//修改時間
	
	@Column(name = "car_type")
	private String carType;		//汽車類型
	
	@Column(name = "car_size")
	private String carSize;		//汽車大小
	
	@Column(name = "total_vehicles")
	private Integer totalVehicles; //車輛總數
	
	@Column(name = "available_vehicles")
	private Integer availableVehicles; //可使用數輛

}
