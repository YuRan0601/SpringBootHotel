package com.cloudSerenityHotel.product.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
	
	private List<Map<String, Object>> productToMapList(List<Products> products){
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		
		for (Products bean : products) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
			
			map.put("productId", bean.getProductId());
			map.put("productName", bean.getProductName());
			map.put("price", bean.getPrice());
			map.put("specialPrice", bean.getSpecialPrice());
			map.put("createdAt", date.format(bean.getCreatedAt()));
			map.put("updatedAt", date.format(bean.getUpdatedAt()));
			map.put("description", bean.getDescription());
			map.put("status", bean.getStatus());
			
			map.put("OneToManyProductImages", productImagesToMapList(bean.getProductImages()));
			map.put("ManyToManyCategories", categoriesToMapList(bean.getCategories()));
			
			mapList.add(map);
		}
		return mapList;
	}
	
	private List<Map<String, Object>> productImagesToMapList(List<ProductImages> image){
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		
		for (ProductImages bean : image) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("imageId", bean.getImageId());
			map.put("imageUrl", bean.getImageUrl());
			map.put("isPrimary", bean.getIsPrimary());
			map.put("createdAt", bean.getCreatedAt());
			
			mapList.add(map);
			
		}
		return mapList;
	}
	
	private List<Map<String, Object>> categoriesToMapList(List<Categories> categories){
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		
		for (Categories bean : categories) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("categoryId", bean.getCategoryId());
			map.put("categoriesName", bean.getCategoriesName());
			
			mapList.add(map);
		}
		return mapList;
	}
	
	// 顯示全部商品
	@Override
	public List<Map<String, Object>> selectAllProduct() {
		List<Products> getAll = productDao.findAll();
		return productToMapList(getAll);
	}
	
	// 顯示單筆商品
	@Override
	public List<Map<String, Object>> selectProduct(Integer productId) {
		Optional<Products> product = productDao.findById(productId);
		
		if (product.isPresent()) {
			return productToMapList(Arrays.asList(product.get()));
		}
		return null;
	}
	
	@Override
	public List<Map<String, Object>> searchProductsByName(String name) {
		List<Products> getProduct =  productDao.findByProductNameContainingIgnoreCase(name);
		return productToMapList(getProduct);
	}
	
	// 顯示全部分類
	@Override
	public List<Map<String, Object>> selectAllCategories() {
		List<Categories> getAll = categoriesDao.findAll();
		return categoriesToMapList(getAll);
	}

	//取得該分類的所有商品(上架的)
	@Override
	public List<Map<String, Object>> findProductCategoryById(Integer categoryId, Integer status) {
	    List<Products> products = productDao.findByCategories_CategoryIdAndStatus(categoryId, status);
	    return productToMapList(products);
		
//		Optional<Categories> category = categoriesDao.findById(categoryId);
//		
//		if (category.isPresent()) {
//			List<Products> products = category.get().getProducts();// 取得該分類的所有商品
//			return productToMapList(products);
//		}
//		return null;
	}


	// 顯示上架or下架商品
	@Override
	public List<Map<String, Object>> selectProductStatus(Integer status) {
		List<Products> productsTatus = productDao.findByStatus(status);
		return productToMapList(productsTatus);
	}

	 // 新增分類
	 @Override
	 public int insertCategory(List<Categories> categories) {
		categoriesDao.saveAll(categories);
	 	return 0;
	 }
	 
	 // 新增商品
	 @Override
	 public int insertProduct(Products products) {
	     // 儲存分類
	     for (Categories category : products.getCategories()) {
	         Categories existingCategory = categoriesDao.findByCategoriesName(category.getCategoriesName())
	                                       .orElse(category);
	         existingCategory.getProducts().add(products);
	     }
	     
	     // 儲存商品與圖片
	     productDao.save(products);
	     for (ProductImages image : products.getProductImages()) {
	         productImagesDao.save(image);
	     }
	     return 1;
	 }



	@Override
	public int uploadImage(Products products,ProductImages images) {
        
        images.setProducts(products);
        products.getProductImages().add(images);
//        productImagesDao.save(images);
  
	  return 0;
	 }
	
	// 刪除商品
	 @Override
	 public int deleteProduct(Integer productId) {
	  productDao.deleteById(productId);
	  return 0;
	 }
	
	// 刪除圖片
	@Override
	public int deleteImage(Integer imageId) {
	  productImagesDao.deleteById(imageId);
	return 0;
	}
 

	//修改上架、下架
	@Override
	public int updateStatus(Products products) {
		Optional<Products> getOne = productDao.findById(products.getProductId());
		Products productId = getOne.get();
		
		productId.setStatus(products.getStatus());
		productDao.save(productId);
		return 0;
	}


	// 修改商品
	@Override
	public int updateProduct(Products products) {
	    Optional<Products> getOne = productDao.findById(products.getProductId());
	    if (!getOne.isPresent()) {
	        return 0;
	    }
	    Products existingProduct = getOne.get();

	    // 清除舊的分類關聯並保存
	    existingProduct.getCategories().clear();
	    productDao.save(existingProduct);

	    // 儲存新的分類
	    List<Categories> updatedCategories = new ArrayList<>();
	    for (Categories category : products.getCategories()) {
	        Categories existingCategory = categoriesDao.findByCategoriesName(category.getCategoriesName()).orElse(null);
	        if (existingCategory == null) {
	            existingCategory = new Categories();
	            existingCategory.setCategoriesName(category.getCategoriesName());
	            categoriesDao.save(existingCategory);
	        }
	        if (!existingCategory.getProducts().contains(existingProduct)) {
	            existingCategory.getProducts().add(existingProduct);
	        }
	        if (!existingProduct.getCategories().contains(existingCategory)) {
	            existingProduct.getCategories().add(existingCategory);
	        }
	        updatedCategories.add(existingCategory);
	    }

	    existingProduct.setCategories(updatedCategories);
	    productDao.save(existingProduct);

	    return 1;
	}



 // 商品狀態(待改)
/* @Override
 public int updateStatus(Products products) {
  Optional<Products> getOne = productDao.findById(products.getProductId());
  Products productId = getOne.get();
  
  productId.setStatus(products.getStatus());
  productDao.save(productId);
  return 0;
 }*/
 
	// 多商品查詢_for Order用
	@Override
	public List<Products> findProductsById(List<Integer> productIds) {
		// 使用 JPA Repository 的 findAllById 方法查詢多個產品
		return productDao.findAllById(productIds);
	}
	
	// 簡化版_for Cart用
	@Override
	public Optional<Products> findById(Integer productId) {
		return productDao.findById(productId);
	}






}
