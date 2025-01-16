package com.cloudSerenityHotel.product.service;

import java.util.List;

import com.cloudSerenityHotel.product.model.Categories;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;


public interface ProductService {

	Products selectProduct(Integer productId);
	List<Products> selectAllProduct();
	int insertProduct(Products products,Categories categories);
	int uploadImage(Products products,ProductImages Images);
	int deleteProduct(Integer productId);
	int updateProduct(Products products,Categories categories);
	
// 有關訂單方法
	// 多商品查詢
	List<Products> findProductsById(List<Integer> productIds);
}
