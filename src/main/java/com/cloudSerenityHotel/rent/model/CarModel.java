package com.cloudSerenityHotel.rent.model;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity 
@Table(name = "cars_model")
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
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT+8")
	private Date createdAt;		//創建時間
	
	@Column(name = "updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT+8")
	private Date updatedAt;		//修改時間
	
	@Column(name = "car_type")
	private String carType;		//汽車類型
	
	@Column(name = "car_size")
	private String carSize;		//汽車大小
	
	
	public CarModel() {
	}


	public CarModel(int carId, String carModel, String description, String brand, int engineDisplacement,
			int seatingCapacity, Date createdAt, Date updatedAt, String carType, String carSize) {
		super();
		this.carModelId = carId;
		this.carModel = carModel;
		this.description = description;
		this.brand = brand;
		this.engineDisplacement = engineDisplacement;
		this.seatingCapacity = seatingCapacity;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.carType = carType;
		this.carSize = carSize;
	}


	public int getCarId() {
		return carModelId;
	}


	public void setCarId(int carId) {
		this.carModelId = carId;
	}


	public String getCarModel() {
		return carModel;
	}


	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public int getEngineDisplacement() {
		return engineDisplacement;
	}


	public void setEngineDisplacement(int engineDisplacement) {
		this.engineDisplacement = engineDisplacement;
	}


	public int getSeatingCapacity() {
		return seatingCapacity;
	}


	public void setSeatingCapacity(int seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public Date getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}


	public String getCarType() {
		return carType;
	}


	public void setCarType(String carType) {
		this.carType = carType;
	}


	public String getCarSize() {
		return carSize;
	}


	public void setCarSize(String carSize) {
		this.carSize = carSize;
	}


	@Override
	public String toString() {
		return "CarModel [carId=" + carModelId + ", carModel=" + carModel + ", description=" + description + ", brand="
				+ brand + ", engineDisplacement=" + engineDisplacement + ", seatingCapacity=" + seatingCapacity
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", carType=" + carType + ", carSize="
				+ carSize + "]";
	}
	
	

}
