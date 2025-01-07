package com.cloudSerenityHotel.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudSerenityHotel.product.model.Products;

public interface ProductRepository extends JpaRepository<Products, Integer> {

}
