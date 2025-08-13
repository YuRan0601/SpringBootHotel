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

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Orders")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	// 訂單唯一編號，自動增長
	private Integer orderId; 
	
	@Column(name = "userid")
	// 使用者編號
	private Integer userId; 
	
	@Column(name = "receive_name")
	 // 收件人名稱
	private String receiveName;
	
	@Column(name = "email")
	// 電子信箱
	private String email; 
	
	@Column(name = "phone_number")
	// 電話號碼
	private String phoneNumber; 
	
	@Column(name = "address")
	// 地址
	private String address; 
	
	@Column(name = "order_status")
	// 訂單狀態
	private String orderStatus; 
	
	@Column(name = "payment_method")
	// 付款方式
	private String paymentMethod; 
	
	@Column(name = "total_amount")
	// 訂單總金額(原價*數量)
	private BigDecimal totalAmount; 
	
//	@Column(name = "points_discount")
//	// 點數折抵
//	private int pointsDiscount; 
//	
//	@Column(name = "discount_amount")
//	// 訂單總折扣金額
//	private BigDecimal discountAmount; 
	
	@Column(name = "final_amount")
	// 訂單最終金額(特價*數量) ，如果沒有特價就(原價*數量)
	private BigDecimal finalAmount;
	
	@Column(name = "order_date")
	// 訂單建立日期，改為 Timestamp
	private Timestamp orderDate; 
	
	@Column(name = "updated_at")
	// 更新時間，改為 Timestamp
	private Timestamp updatedAt; 
	
	/*
	 * @PrePersist 會在 第一次把這個 Entity 存進資料庫 前自動呼叫。
	 * 設定 createdAt 和 updatedAt 都用同一個時間戳，第一次存檔就搞定兩個欄位。
	 */
	@PrePersist
    public void prePersist() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.orderDate = now;
        this.updatedAt = now;
    }
	
	/*
	 * 在 JPA Entity 裡加一個方法，並用 @PreUpdate 標註，
	 * 當 Hibernate 發現這個 Entity 要被更新時，就會自動呼叫這個方法。
	 */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

	// 訂單與訂單細項的雙向關聯
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
	// 關聯的訂單細項
	private Set<OrderItems> orderItems; 
	
	// 新增 recipient 屬性，用來儲存收件人資料
    @Transient // 這裡使用 @Transient 表示這不是資料庫的欄位，只是在應用層使用
    private MemberForCartFrontendDTO recipient; // 收件人資料

}