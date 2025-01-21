package com.cloudSerenityHotel.product.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ProductImages")
@Component
public class ProductImages implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Integer imageId;
	
//  下面有JoinColumn	這邊就不用打了 因為會重複
//	@Column(name = "product_id")
//	private Integer product_id;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "is_primary")
	private Boolean isPrimary;
//	private boolean is_primary; 不能用這個會報錯，一定要isIs開頭
	
	@Column(name = "created_at", insertable = false, updatable =  false)
	private Timestamp createdAt;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Products products;
}
