package com.cloudSerenityHotel.product.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.cloudSerenityHotel.product.dao.ProductImagesRepository;
import com.cloudSerenityHotel.product.dao.ProductRepository;
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
	private ProductImagesRepository productImagesDao;
	
	@Autowired
	private CategoriesRepository categoriesDao;
	
	@Autowired
	private ProductRepository productDao;

	
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
	
    @GetMapping("/search")
    public List<Map<String, Object>> searchProducts(@RequestParam String name) {
        return productService.searchProductsByName(name);
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
	
	
	// 新增分類
	@PostMapping("/insert/category")
	public int insertCategory(@RequestBody List<Categories> categories) {
		return productService.insertCategory(categories);
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
		        image.setIsPrimary(false);
		        productService.uploadImage(products, image);
		    }
		}

	    return productService.insertProduct(products);
	}

	
	// 刪除
	@DeleteMapping("/delete/{productId}")
	public int delete(@PathVariable int productId) {
		return productService.deleteProduct(productId);
	}
	
	@DeleteMapping("/delete/image/{imageId}")
	public int deleteImage(@PathVariable int imageId) {
		return productService.deleteImage(imageId);
	}
	
	// 取的某商品資料 顯示在修改頁面
	@GetMapping("/getUpdate/{productId}")
	public List<Map<String, Object>> getUpdate(@PathVariable int productId) {
		return productService.selectProduct(productId);
	}
	
	// 修改商品
	@PutMapping(value = "/update/productWithImagesAndCategories/{productId}", consumes = {"multipart/form-data"})
	public int updateProductWithImagesAndCategories(
	    @PathVariable int productId, 
	    @RequestPart("product") Products products,
	    @RequestPart("imageCover") MultipartFile imageCover,
	    @RequestPart(value = "images", required = false) List<MultipartFile> images) 
	    throws IllegalStateException, IOException {

	    // 1. 確保 `productId` 存在於資料庫
	    Optional<Products> existingProductOptional = productDao.findById(productId);
	    if (!existingProductOptional.isPresent()) {
	        return 0; // 產品不存在
	    }
	    Products existingProduct = existingProductOptional.get();

	    // 2. 更新基本資訊
	    existingProduct.setProductName(products.getProductName());
	    existingProduct.setDescription(products.getDescription());
	    existingProduct.setPrice(products.getPrice());
	    existingProduct.setSpecialPrice(products.getSpecialPrice());
	    existingProduct.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

	    // 3. 清除舊的分類關聯並保存
	    existingProduct.getCategories().clear();
	    productDao.save(existingProduct);
	    
	    // 4. 儲存新的分類關聯
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

	    // 設置更新後的分類
	    existingProduct.setCategories(updatedCategories);
	    productDao.save(existingProduct);

	    // 5. 上傳商品封面
	    String coverFileName = imageCover.getOriginalFilename();
	    String coverSaveFileDir = "src/main/webapp/static/product/uploadImage/";
	    File coverSaveFilePath = new File(coverSaveFileDir, coverFileName);
	    imageCover.transferTo(coverSaveFilePath.getAbsoluteFile());

	    ProductImages coverImage = new ProductImages();
	    coverImage.setImageUrl("static/product/uploadImage/" + coverFileName);
	    coverImage.setIsPrimary(true);
	    coverImage.setProducts(existingProduct);
	    productImagesDao.save(coverImage);

	    // 6. 上傳其他商品圖片
	    if (images != null && !images.isEmpty()) {
	        for (MultipartFile file : images) {
	            String fileName = file.getOriginalFilename();
	            File saveFilePath = new File(coverSaveFileDir, fileName);
	            file.transferTo(saveFilePath.getAbsoluteFile());

	            ProductImages image = new ProductImages();
	            image.setImageUrl("static/product/uploadImage/" + fileName);
	            image.setIsPrimary(false);
	            image.setProducts(existingProduct);
	            productImagesDao.save(image);
	        }
	    }

	    return productService.updateProduct(existingProduct);
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
