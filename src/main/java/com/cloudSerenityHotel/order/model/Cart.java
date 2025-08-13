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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
	private Timestamp cartDate; 
    
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
        this.cartDate = now;
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
	
    // 購物車與購物車細項的雙向關聯
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart", cascade = CascadeType.ALL)
    // 關聯的購物車細項
    private Set<CartItems> cartItems;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    // 使用者 ID(需要Member 中的電話、地址)
    private Member member;
    
    /*
     * User 那段可以完全刪掉，因為 Member 本身已經一對一關聯到 User，
     * 要用 User 資料就透過 cart.getMember().getUser() 去拿就好。
     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userid")
//    // 使用者 ID(需要User 中的使用者名稱、電子信箱)
//    private User user;
}
