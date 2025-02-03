package com.cloudSerenityHotel.product.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudSerenityHotel.product.model.Products;

public interface ProductRepository extends JpaRepository<Products, Integer> {

	List<Products> findByStatus(Integer status);
	List<Products> findByCategories_CategoryIdAndStatus(Integer categoryId, Integer status);
	
	// 購物車 -> 訂單, 新增根據 productId 查詢單一商品的方法
    Optional<Products> findById(Integer productId);  // 這樣的話返回的是 Optional<Products>
}
