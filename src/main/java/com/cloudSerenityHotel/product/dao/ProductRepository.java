package com.cloudSerenityHotel.product.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudSerenityHotel.product.model.Products;

public interface ProductRepository extends JpaRepository<Products, Integer> {

	List<Products> findByStatus(Integer status);
}
