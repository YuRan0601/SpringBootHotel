package com.cloudSerenityHotel.product.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Products")
@Component
public class Products implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "special_price")
	private BigDecimal specialPrice;
	
	@Column(name = "created_at", insertable = false, updatable =  false)//hibernate新增修改時關掉 防止null
	private Timestamp createdAt;
	
	@Column(name = "updated_at", insertable =  false)
	private Timestamp updatedAt;
	
    @Column(name = "status")
    private int status;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "products",cascade = CascadeType.ALL)
	private List<ProductImages> productImages = new ArrayList<ProductImages>();
	
	@ManyToMany(mappedBy = "products",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
	private List<Categories> categories = new ArrayList<Categories>();

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "products",cascade = CascadeType.ALL)
//	private Set<ProductImages> productImages = new LinkedHashSet<ProductImages>();
//	
//	
//	@ManyToMany(mappedBy = "products",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
//	private Set<Categories> categories = new HashSet<Categories>();
	
}
