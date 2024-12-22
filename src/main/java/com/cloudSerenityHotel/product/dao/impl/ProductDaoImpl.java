package com.cloudSerenityHotel.product.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.cloudSerenityHotel.product.model.Categories;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.utils.HibernateUtil;


public class ProductDaoImpl implements ProductDao {
	
	private SessionFactory factory;
	
	public ProductDaoImpl() {
		this.factory = HibernateUtil.getSessionFactory();
	}

	
	//查詢單筆
	@Override
	public Products selectProduct(Integer product_id) {
		Products products = null;
		try {
			Session session = factory.getCurrentSession();
			
			products = session.find(Products.class, product_id);
			
//			products.getProductImages();
//			products.getCategories();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
		
	}
		
		//顯示全部商品
		@Override
		public List<Products> selectAllProduct() {
				
			Session session = factory.getCurrentSession();
			Query<Products> query = session.createQuery("FROM Products",Products.class);
			List<Products> product = query.getResultList();
//			System.out.println("Products+ ProductImages:" + product);//不能用這個會觸發toString錯誤
			return product;
		}
		
		
		//新增商品
		@Override
		public int insertProduct(Products products,Categories categories) {
			
			try {
				Session session = factory.getCurrentSession();
				products.getCategories().add(categories);
				
				categories.getProducts().add(products);
				
				session.persist(products);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}

		//上傳圖片
		@Override
		public int uploadImage(Products products,ProductImages Images) {
			
			try {
				Session session = factory.getCurrentSession();
				// 因為前端還沒用，可以顯示多張圖片，然後設定哪個是主圖片(封面)，所以暫時先設定最新上傳的圖片設為封面
				// 查詢該產品是否已有主圖片
		        ProductImages currentPrimaryImage = session.createQuery("FROM ProductImages WHERE products = :Images AND isIs_primary = true", ProductImages.class)
		                                                   .setParameter("Images", products)
		                                                   .uniqueResult();
		        // 如果已有主圖片，設為非主圖片
		        if (currentPrimaryImage != null) {
		            currentPrimaryImage.setIs_primary(false);
		            session.merge(currentPrimaryImage);
		        }

		        // 設定新圖片為主圖片並與產品關聯
		        Images.setProducts(products);
		        Images.setIs_primary(true);
		        session.persist(Images);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return 0;
		}


		//刪除商品
		@Override
		public int deleteProduct(Integer product_id) {
			
			try {
				Session session = factory.getCurrentSession();
				Products products = session.find(Products.class, product_id);
				session.remove(products);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;

		}


		//修改商品
		@Override
		public int updateProduct(Products products,Categories categories) {
			
			try {
				Session session = factory.getCurrentSession();
				Products productId = session.find(Products.class, products.getProduct_id());
//				Categories categoriesId = session.find(Categories.class, products.getProduct_id());
				productId.getCategories().add(categories);
				categories.getProducts().add(productId);
				
				session.merge(productId);
				session.merge(products);
//				productId.setName(products.getName());
//				productId.setDescription(products.getDescription());
//				productId.setPrice(products.getPrice());
//				productId.setSpecial_price(products.getSpecial_price());
//				productId.setCategories(products.getCategories());
//				
//				session.merge(productId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}



	
}
