package com.cloudSerenityHotel.product.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudSerenityHotel.product.model.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
	Optional<Categories> findByCategoriesName(String categoriesName);
}
