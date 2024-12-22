package com.cloudSerenityHotel.user.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USERID")
	private Integer userId; //使用者編號
	
	@Column(name = "USER_NAME")
	private String userName; //使用者名稱
	
	@Column(name = "EMAIL")
	private String email; //電子信箱
	
	@Column(name = "PASSWORD")
	private String password; //密碼
	
	@Column(name = "UPDATE_TIME")
	private LocalDateTime accountUpdateTime; //帳號更新時間
	
	@Column(name = "USER_STATUS")
	private String userStatus; //使用者狀態
	
	@Column(name = "USER_IDENTITY")
	private String userIdentity; //使用者身分
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = true)  // 設置 nullable = true 使得 Member 可以為 null
	private Member member;

}
