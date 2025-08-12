package com.cloudSerenityHotel.order.model;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import com.cloudSerenityHotel.user.model.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "Cart")
public class Cart implements Serializable{
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    // 購物車唯一編號
    private Integer cartId; 
    
//    @Column(name = "userid")
//    // 使用者 ID
//    private Integer userId; 
    
    @Column(name = "created_at")
    // 建立日期，改為 Timestamp
	private Timestamp createdAt; 
    
	@Column(name = "updated_at")
	// 更新時間，改為 Timestamp
	private Timestamp updatedAt; 
	
    // 購物車與購物車細項的雙向關聯
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart", cascade = CascadeType.ALL)
    // 關聯的訂單細項
    private Set<CartItems> CartItemsBeans; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    // 使用者 ID
    private Member member;
}
