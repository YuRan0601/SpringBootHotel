package com.cloudSerenityHotel.user.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "members")
public class Member implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer userId; //使用者編號
	
	@Column(name = "gender")
	private String gender; //性別
	
	@Column(name = "birthday")
	private LocalDate birthday; //生日
	
	@Column(name = "phone")
	private String phone; //電話
	
	@Column(name = "personal_id_no")
	private String personalIdNo; //身分證字號
	
	@Column(name = "country")
	private String country; //國家
	
	@Column(name = "address")
	private String address; //地址
	
	@Column(name = "passport_no")
	private String passportNo; //護照號碼
	
	@Column(name = "register_date")
	private LocalDateTime registerDate; //註冊時間
	
	@Column(name = "update_time")
	private LocalDateTime dataUpdateTime; //資料更新時間
	
	@JsonIgnore
	@MapsId
	@OneToOne
	@JoinColumn(name = "userid")
	private User user;
}
