package com.cloudSerenityHotel.product.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cloudSerenityHotel.product.model.Categories;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;


public interface ProductService {

	List<Map<String, Object>> selectProduct(Integer productId);
	List<Map<String, Object>> selectAllProduct();
	List<Map<String, Object>> selectAllCategories();
	List<Map<String, Object>> findProductCategoryById(Integer categoryId, Integer status); //取得該分類的所有商品(上架的)
	List<Map<String, Object>> selectProductStatus(Integer status); //顯示上架or下架
	int insertProductAndCategories(Products products,Categories categories);
	int insertProduct(Products products);
	int insertCategories(Categories categories);
	int uploadImage(Products products,ProductImages Images);
	int deleteProduct(Integer productId);
	int updateProduct(Products products,Categories categories);
	int updateStatus(Products products); //更改上下架狀態
	
	// 多商品查詢_for Order用
    List<Products> findProductsById(List<Integer> productIds);
    // 簡化版_for Cart用
    Optional<Products> findById(Integer productId);
}
