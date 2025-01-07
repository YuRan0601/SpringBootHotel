package com.cloudSerenityHotel.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudSerenityHotel.product.model.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {

}
