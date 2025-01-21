package com.cloudSerenityHotel.order.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.cloudSerenityHotel.product.model.Products;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OrderItems")
public class OrderItemsBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orderitem_id")
	private Integer orderitemId; // 關聯唯一編號，自動增長
	
	//@Column(name = "order_id", insertable = false, updatable = false) // 防止重複映射
	//private Integer orderId; // 所屬訂單ID
	
	//@Column(name = "product_id")
	//private Integer productId; // 購買商品的ID
	
	@Column(name = "quantity")
	private Integer quantity; // 購買數量
	
	@Column(name = "unit_price")
	private BigDecimal unitPrice; // 單價（訂單中確定的價格）
	
	@Column(name = "discount")
	private BigDecimal discount; // 單項商品的折扣金額
	
	@Column(name = "subtotal")
	private BigDecimal subtotal; // 小計（(UnitPrice - Discount) * Quantity）
	
	// 多對一的關聯：一個訂單細項對應一個訂單
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false) // 
	@JsonIgnore // 不序列化 `OrderBean`，避免循環
	private OrderBean order; // 這樣做會將 `order_id` 映射到 `Order` 實體
	
	// 多對一：商品
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler","productImages", "categories"}) // 避免無限嵌套
//	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 只忽略代理相關字段_忽略 Hibernate Proxy
	private Products products; // 與商品實體的關聯
}