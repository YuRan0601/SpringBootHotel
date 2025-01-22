package com.cloudSerenityHotel.product.service;

import java.util.List;
import java.util.Map;

import com.cloudSerenityHotel.product.model.Categories;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;


public interface ProductService {

	List<Map<String, Object>> selectProduct(Integer productId);
	List<Map<String, Object>> selectAllProduct();
	int insertProduct(Products products,Categories categories);
	int uploadImage(Products products,ProductImages Images);
	int deleteProduct(Integer productId);
	int updateProduct(Products products,Categories categories);
	int updateStatus(Products products);
}
