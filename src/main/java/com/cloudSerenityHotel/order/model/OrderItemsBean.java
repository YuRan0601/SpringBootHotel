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
	@JoinColumn(name = "order_id", nullable = false)
	/*
	 * nullable = false 的功能與作用
	 * 資料庫層面：
	 * 在資料庫中，這會在該欄位上加上 NOT NULL 限制。
	 * 意思是，資料庫不允許插入或更新時該欄位為 NULL。
	 * 如果嘗試插入或更新一個 NULL 值，資料庫會拋出錯誤。
	 * 應用層面：
	 * 確保應用程式邏輯不會存入無效的 NULL 值。
	 * 提升資料的完整性，避免因為空值導致邏輯錯誤或數據不一致。
	 */
	/*
	 * nullable = false =>
	 * 如果欄位有設定預設值（例如 BigDecimal.ZERO），則即使沒有賦值也不會是 NULL，這與 nullable = false 的效果互補。
	 * 資料庫層面： 它會在資料庫中生成的 order_id 外鍵列上加上 NOT NULL 限制。這表示在對應的數據行中，order_id 的值必須存在，不能為空（NULL）。
	 * 邏輯層面： 強制要求每個 OrderItemsBean 必須關聯到某個 OrderBean，即每個訂單項必須屬於一個訂單。
	 */
	// 改使用DTO
	//@JsonIgnore // 不序列化 `OrderBean`，避免循環
	private OrderBean order; // 這樣做會將 `order_id` 映射到 `Order` 實體
	
	// 多對一：商品
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	// 改使用DTO
//	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler","productImages", "categories"}) // 避免無限嵌套
//	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 只忽略代理相關字段_忽略 Hibernate Proxy
	private Products products; // 與商品實體的關聯
}