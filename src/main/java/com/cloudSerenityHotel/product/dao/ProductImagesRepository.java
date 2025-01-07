package com.cloudSerenityHotel.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;

public interface ProductImagesRepository extends JpaRepository<ProductImages, Integer> {

	ProductImages findByProductsAndIsprimaryTrue(Products products);
}
