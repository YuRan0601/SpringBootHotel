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
@Table(name = "CartItems")
public class CartItems implements Serializable{
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId; // 購物車商品 ID

    //@Column(name = "cart_id")
    //private Integer cartId; // 關聯的購物車 ID

	//@Column(name = "product_id")
	//private Integer productId; // 購買商品的ID

    @Column(name = "quantity")
    private int quantity; // 商品數量

    @Column(name = "unit_price")
    private BigDecimal unitPrice; // 當時加入購物車的單價

    @Column(name = "discount")
    private BigDecimal discount; // 折扣金額

    @Column(name = "subtotal")
    private BigDecimal subtotal; // 小計（(UnitPrice - Discount) * Quantity）

    @Column(name = "is_valid")
    private int isValid = 0; // 商品狀態(0=有效, 1=無效_商品下架, 2=無效_價格變動)

    // 多對一的關聯：一個購物車細項對應一個購物車
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
    private Cart cart; // 這樣做會將 `cart_id` 映射到 `Cart` 實體
    
    // 多對一的關聯：關聯到商品
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Products products; // 與商品表的關聯
}
