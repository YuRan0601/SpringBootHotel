package com.cloudSerenityHotel.rent.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagesBean {

	@Id
	@Column(name = "image_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int imageId;

	@Column(name = "car_id")
	private int carId;

	@Column(name = "is_primary")
	private int isPrimary;

	@Column(name = "car_image_url")
	private String imageUrl;
}
