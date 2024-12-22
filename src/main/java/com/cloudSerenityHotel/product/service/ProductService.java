package com.cloudSerenityHotel.product.service;

import java.util.List;

import com.cloudSerenityHotel.product.model.Categories;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;


public interface ProductService {

	Products selectProduct(Integer product_id);
	List<Products> selectAllProduct();
	int insertProduct(Products products,Categories categories);
	int uploadImage(Products products,ProductImages Images);
	int deleteProduct(Integer product_id);
	int updateProduct(Products products,Categories categories);
}
