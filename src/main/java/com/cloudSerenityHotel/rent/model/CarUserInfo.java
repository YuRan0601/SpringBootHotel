package com.cloudSerenityHotel.rent.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserInfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarUserInfo {

		@Id
	    @Column(name = "userid")
	    private int userId; // 使用者編號

	    @Column(name = "personal_id_no")
	    private String personalIdNo; // 身分證字號

	    @Column(name = "user_name")
	    private String userName; // 使用者姓名

	    @Column(name = "email")
	    private String email; // 電子郵件

	    @Column(name = "birthday")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    private LocalDate birthday; // 生日
	    
	    @Column(name = "gender")
	    private String gender;  //性別

	    @Column(name = "phone")
	    private String phone; // 電話號碼

	    @Column(name = "driver_license_number")
	    private String driverLicenseNumber; // 駕駛號碼

	    @Column(name = "booking_id")
	    private Integer bookingId; // 訂房ID

	    @Column(name = "booking_status")
	    private String bookingStatus; // 訂房狀態

	    @Column(name = "LicenseImage")
	    private String licenseImage; // 駕照照片

	    @Column(name = "passport_no")
	    private String passportNo; // 護照號碼
	
}
