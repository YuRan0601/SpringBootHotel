package com.cloudSerenityHotel.product.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.cloudSerenityHotel.product.dao.CategoriesRepository;
import com.cloudSerenityHotel.product.model.Categories;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;



@RestController
@RequestMapping("/Product")
@CrossOrigin(origins = {"http://localhost:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductController {
    
	@Autowired
	private ProductServiceImpl productService;
	
	@Autowired
	private CategoriesRepository categoriesDao;

	
//	//側邊選單(套版用)
//	@GetMapping("/aside")
//	public String productHome() {
//		return "/common/aside.html";
//	}
	
	//查詢單筆
	@GetMapping("/select/{productId}")
	public List<Map<String, Object>> select(@PathVariable int productId) {
		return productService.selectProduct(productId);
	}
	
	//顯示上架or下架
	@GetMapping("/select/productStatus/{status}")
	public List<Map<String, Object>> selectProductStatus(@PathVariable int status) {
		return productService.selectProductStatus(status);
	}
	
	//顯示全部分類
	@GetMapping("/select/allCategories")
	public List<Map<String, Object>> selectAllCategories() {
		return productService.selectAllCategories();
	}
	
	//取得該分類的所有商品(上架的)
	@GetMapping("/select/productCategory/{categoryId}")
	public List<Map<String, Object>> findProductCategoryById(@PathVariable int categoryId) {
		return productService.findProductCategoryById(categoryId, 1);
	}
	
	//新增
	@PostMapping("/insertProducts")
	public int insertProducts(@RequestBody Products products) throws IllegalStateException, IOException {
		return productService.insertProduct(products);
	}
	
	@PostMapping("/insertCategories")
	public int insertCategories(@RequestBody Categories categories) {
		return productService.insertCategories(categories);
	}
	
	
	@PostMapping(value = "/insertProductWithImagesAndCategories", consumes = {"multipart/form-data"})
	public int insertProductWithImagesAndCategories(@RequestPart("product") Products products,@RequestPart("imageCover") MultipartFile imageCover,@RequestPart(value = "images", required = false) List<MultipartFile> images) throws IllegalStateException, IOException {
	    // 暫存更新後的分類
	    List<Categories> updatedCategories = new ArrayList<>();
	    for (Categories category : products.getCategories()) {
	        Categories existingCategory = categoriesDao.findByCategoriesName(category.getCategoriesName()).orElse(null);
	        if (existingCategory == null) {
	            existingCategory = new Categories();
	            existingCategory.setCategoriesName(category.getCategoriesName());
	            categoriesDao.save(existingCategory);
	        }
	        updatedCategories.add(existingCategory); // 加入暫存清單
	    }
	    products.setCategories(updatedCategories); // 將更新後的分類設回產品
	    
	    //上傳商品封面
	    String CoverFileName = imageCover.getOriginalFilename();
	    String CoverSaveFileDir = "src/main/webapp/static/product/uploadImage/";
	    File CoverSaveFilePath = new File(CoverSaveFileDir, CoverFileName);
	    imageCover.transferTo(CoverSaveFilePath.getAbsoluteFile());
	    
	    ProductImages CoverImage = new ProductImages();
	    CoverImage.setImageUrl("static/product/uploadImage/" + CoverFileName);
	    CoverImage.setIsPrimary(true);
	    productService.uploadImage(products, CoverImage);
	    
	    // 上傳其他商品圖片
	    if (images != null && !images.isEmpty()) {
		    for (int i = 0; i < images.size(); i++) {
		        MultipartFile file = images.get(i);
		        String fileName = file.getOriginalFilename();
		        String saveFileDir = "src/main/webapp/static/product/uploadImage/";
		        File saveFilePath = new File(saveFileDir, fileName);
		        file.transferTo(saveFilePath.getAbsoluteFile());

		        ProductImages image = new ProductImages();
		        image.setImageUrl("static/product/uploadImage/" + fileName);
//		        image.setIsPrimary(i == 0); // 第一張設為主圖
		        productService.uploadImage(products, image);
		    }
		}

	    return productService.insertProduct(products);
	}

	
	//刪除
	@DeleteMapping("/delete/{productId}")
	public int delete(@PathVariable int productId) {
		return productService.deleteProduct(productId);
	}
	
	//取的某商品資料 顯示在修改頁面
	@GetMapping("/getUpdate/{productId}")
	public List<Map<String, Object>> getUpdate(@PathVariable int productId) {
		return productService.selectProduct(productId);
	}
	
	//修改
	@PutMapping("/update/{productId}")
	public int update(@RequestBody Products products, @RequestBody Categories categories, @RequestBody ProductImages image, @RequestPart MultipartFile mf) throws IllegalStateException, IOException {
		
		
		//上傳圖片
		String fileName = mf.getOriginalFilename();
		String saveFileDir = "src/main/webapp/static/product/uploadImage/";
		
		File saveFilePath = new File(saveFileDir, fileName);
		mf.transferTo(saveFilePath.getAbsoluteFile());
		
        //把圖片路徑存到資料庫表格裡
        image.setImageUrl("/CloudSerenityHotel/static/product/uploadImage/" + fileName);
        productService.uploadImage(products,image);
		
		return productService.updateProduct(products,categories);
	}
	
	//修改上架、下架
	@PutMapping("/updateStatus/{productId}")
	public int updateStatus(@PathVariable int productId, @RequestParam int status) {
	    Products product = new Products();
	    product.setProductId(productId);
	    product.setStatus(status); // 上架或下架狀態，0表示下架，1表示上架
	    
	    return productService.updateStatus(product);
	}
}
