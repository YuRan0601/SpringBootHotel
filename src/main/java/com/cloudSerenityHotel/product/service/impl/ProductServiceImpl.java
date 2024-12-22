package com.cloudSerenityHotel.product.service.impl;

import java.util.List;

import com.cloudSerenityHotel.product.dao.impl.ProductDao;
import com.cloudSerenityHotel.product.dao.impl.ProductDaoImpl;
import com.cloudSerenityHotel.product.model.Categories;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.ProductService;




public class ProductServiceImpl implements ProductService{

	ProductDao productDao = new ProductDaoImpl();
	
	@Override
	public Products selectProduct(Integer product_id) {
		return productDao.selectProduct(product_id);
	}
	
	@Override
	public List<Products> selectAllProduct() {
		return productDao.selectAllProduct();
	}
	

	@Override
	public int insertProduct(Products products,Categories categories) {
		return productDao.insertProduct(products,categories);
	}

	@Override
	public int uploadImage(Products products,ProductImages Images) {
		return productDao.uploadImage(products,Images);
	}

	@Override
	public int deleteProduct(Integer product_id) {
		return productDao.deleteProduct(product_id);
	}

	@Override
	public int updateProduct(Products products,Categories categories) {
		return productDao.updateProduct(products,categories);
	}



}
