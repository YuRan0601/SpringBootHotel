package com.cloudSerenityHotel.rent.model;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "car_details")
public class CarDetails {
	
	@Id
	@Column(name = "car_id")
	private String carId; //汽車ID
	
	@Column(name = "carmodel_id")
	private int carMadelId; //車型ID
	
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
	private Date createdAt; //創建時間
	
	@Column(name = "updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updatedAt; //更新時間
	
	public CarDetails() {
	}

	public CarDetails(String carId, int carMadelId, String licensePlate, String colorOptions, int year, String status,
			Date createdAt, Date updatedAt) {
		super();
		this.carId = carId;
		this.carMadelId = carMadelId;
		this.licensePlate = licensePlate;
		this.colorOptions = colorOptions;
		this.year = year;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public int getCarMadelId() {
		return carMadelId;
	}

	public void setCarMadelId(int carMadelId) {
		this.carMadelId = carMadelId;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getColorOptions() {
		return colorOptions;
	}

	public void setColorOptions(String colorOptions) {
		this.colorOptions = colorOptions;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Override
	public String toString() {
		return "CarDetails [carId=" + carId + ", carMadelId=" + carMadelId + ", licensePlate=" + licensePlate
				+ ", colorOptions=" + colorOptions + ", year=" + year + ", status=" + status + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}

	
}
