package com.cloudSerenityHotel.product.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.product.dao.CategoriesRepository;
import com.cloudSerenityHotel.product.dao.ProductImagesRepository;
import com.cloudSerenityHotel.product.dao.ProductRepository;
import com.cloudSerenityHotel.product.model.Categories;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.ProductService;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productDao;
	
	@Autowired
	private ProductImagesRepository productImagesDao;
	
	@Autowired
	private CategoriesRepository categoriesDao;
	
	
	@Override
	public Products selectProduct(Integer productId) {
		Optional<Products> product = productDao.findById(productId);
		
		if (product.isPresent()) {
			return product.get();
		}
		return null;
	}
	
	@Override
	public List<Products> selectAllProduct() {
		return productDao.findAll();
	}
	

	@Override
	public int insertProduct(Products products,Categories categories) {
		
	    products.getCategories().add(categories);
	    categories.getProducts().add(products);
		
		productDao.save(products);
		categoriesDao.save(categories);
		
		return 0;
	}

	@Override
	public int uploadImage(Products products,ProductImages Images) {
		// 因為前端還沒用，可以顯示多張圖片，然後設定哪個是主圖片(封面)，所以暫時先設定最新上傳的圖片設為封面
		// 查詢該產品是否已有主圖片
		ProductImages currentPrimaryImage = productImagesDao.findByProductsAndIsprimaryTrue(products);
		
        // 如果已有主圖片，設為非主圖片
        if (currentPrimaryImage != null) {
            currentPrimaryImage.setIsprimary(false);
            productImagesDao.save(currentPrimaryImage);
        }
        
        // 設定新圖片為主圖片並與產品關聯
        Images.setProducts(products);
        Images.setIsprimary(true);
        productImagesDao.save(Images);
		
		return 0;
	}

	@Override
	public int deleteProduct(Integer productId) {
		productDao.deleteById(productId);
		return 0;
	}

	@Override
	public int updateProduct(Products products,Categories categories) {
		Optional<Products> getOne = productDao.findById(products.getProductId());
		Products productId = getOne.get();
		
		productId.setName(products.getName());
		productId.setDescription(products.getDescription());
		productId.setPrice(products.getPrice());
		productId.setSpecialPrice(products.getSpecialPrice());
		productId.setCategories(products.getCategories());
		
		productId.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));//修改時間
		
		productId.getCategories().add(categories);
	    categories.getProducts().add(productId);
		
		productDao.save(productId);
//		categoriesDao.save(categories);
		return 0;
	}

// 有關訂單方法
	// 多商品查詢
	@Override
	public List<Products> findProductsById(List<Integer> productIds) {
		// 使用 JPA 提供的 findAllById 方法，一次性查詢多個商品
		return productDao.findAllById(productIds);
	}



}
