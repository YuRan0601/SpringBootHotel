package com.cloudSerenityHotel.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set; // 使用 Java 的 Set

import com.cloudSerenityHotel.order.dto.MemberForCartFrontendDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Orders")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Integer orderId; // 訂單唯一編號，自動增長
	@Column(name = "userid")
	private Integer userId; // 使用者編號
	@Column(name = "receive_name")
	private String receiveName; // 收件人名稱
	@Column(name = "email")
	private String email; // 電子信箱
	@Column(name = "phone_number")
	private String phoneNumber; // 電話號碼
	@Column(name = "address")
	private String address; // 地址
	@Column(name = "order_status")
	private String orderStatus; // 訂單狀態
	@Column(name = "payment_method")
	private String paymentMethod; // 付款方式
	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount = BigDecimal.ZERO; // 預設值確保不為 NULL
	@Column(name = "points_discount")
	private int pointsDiscount; // 點數折抵(不確定的功能 先保留) V 11/17修改
	@Column(name = "discount_amount")
	private BigDecimal discountAmount; // 訂單總折扣金額
	@Column(name = "final_amount", nullable = false)
	private BigDecimal finalAmount = BigDecimal.ZERO; // 確保有初始值
	@Column(name = "order_date")
	private Timestamp orderDate; // 訂單建立日期，改為 Timestamp
	@Column(name = "updated_at")
	private Timestamp updatedAt; // 更新時間，改為 Timestamp

	
	/*public void setUserid(int userId) {
	    this.userId = userId;}*/
	
	// 在新增之前設定 orderDate 和 updatedAt
	@PrePersist
	protected void onCreate() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.orderDate = now;
		this.updatedAt = now;
	}

	// 在更新之前自動設定 updatedAt
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Timestamp(System.currentTimeMillis());
	}

	// 訂單與訂單細項的雙向關聯
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
	// 改使用DTO
	//@JsonIgnoreProperties({"order"}) // 只忽略 `order`，保留 `products`
	private Set<OrderItems> orderItemsBeans; // 關聯的訂單細項
	
	// 新增 recipient 屬性，用來儲存收件人資料
    @Transient // 這裡使用 @Transient 表示這不是資料庫的欄位，只是在應用層使用
    private MemberForCartFrontendDTO recipient; // 收件人資料

}