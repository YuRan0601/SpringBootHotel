package com.cloudSerenityHotel.rent.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageCar {

	@Id
	@Column(name = "image_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imageId;

	@Column(name = "model_id")
	private Integer modelId;

	@Column(name = "car_image_url")
	private String imageUrl;

	@Transient
	private String base64Image;
//	@Column(name = "isMainImage")
//	private int isMainImage;
}
