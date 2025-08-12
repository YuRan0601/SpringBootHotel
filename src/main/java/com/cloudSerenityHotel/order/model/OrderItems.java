package com.cloudSerenityHotel.order.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.cloudSerenityHotel.product.model.Products;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OrderItems")
public class OrderItems implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orderitem_id")
	// 訂單細項_唯一編號，自動增長
	private Integer orderitemId; 
	
	//@Column(name = "order_id", insertable = false, updatable = false) // 防止重複映射
	// 所屬訂單ID
	//private Integer orderId; 
	
	@Column(name = "quantity")
	// 購買數量
	private Integer quantity; 
	
	@Column(name = "unit_price")
	// 單價（訂單中確定的價格）
	private BigDecimal unitPrice; 
	
	@Column(name = "discount")
	// 單項商品的折扣金額
	private BigDecimal discount; 
	
	@Column(name = "subtotal")
	// 小計（(UnitPrice - Discount) * Quantity）
	private BigDecimal subtotal; 
	
	// 多對一的關聯：一個訂單細項對應一個訂單
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	
	// 改使用DTO
	// 不序列化 `OrderBean`，避免循環
	private Order order; // 這樣做會將 `order_id` 映射到 `Order` 實體
	
	// 多對一：商品
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	// 與商品實體的關聯
	private Products products; 
}