package com.cloudSerenityHotel.order.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set; // 使用 Java 的 Set

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// BigDecimalO
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Orders")
public class OrderBean implements java.io.Serializable {
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
	private String orderStatus; // 訂單狀態 V 11/17修改
	@Column(name = "payment_method")
	private String paymentMethod; // 付款方式 V 11/17修改
	@Column(name = "total_amount")
	private BigDecimal totalAmount; // 訂單總金額
	@Column(name = "points_discount")
	private int pointsDiscount; // 點數折抵(不確定的功能 先保留) V 11/17修改
	@Column(name = "discount_amount")
	private BigDecimal discountAmount; // 訂單總折扣金額
	@Column(name = "final_amount")
	private BigDecimal finalAmount; // 訂單最終金額
	@Column(name = "order_date")
	private Timestamp orderDate; // 訂單建立日期，改為 Timestamp
	@Column(name = "updated_at")
	private Timestamp updatedAt; // 更新時間，改為 Timestamp

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
	private Set<OrderItemsBean> orderItemsBeans; // 關聯的訂單細項

	// update 所需之 constructor
	public OrderBean(Integer orderId, Integer userId, String receiveName, String email, String phoneNumber,
			String address, String orderStatus, String paymentMethod, BigDecimal totalAmount, int pointsDiscount,
			BigDecimal discountAmount, BigDecimal finalAmount, Timestamp orderDate, Timestamp updatedAt) {
		this.orderId = orderId;
		this.userId = userId;
		this.receiveName = receiveName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.orderStatus = orderStatus;
		this.paymentMethod = paymentMethod;
		this.totalAmount = totalAmount;
		this.pointsDiscount = pointsDiscount;
		this.discountAmount = discountAmount;
		this.finalAmount = finalAmount;
		this.orderDate = orderDate; // 保持原創建時間
		this.updatedAt = updatedAt; // 更新時間
	}
}