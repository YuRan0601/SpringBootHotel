package com.cloudSerenityHotel.product.controller;

import jakarta.persistence.PostLoad;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.cloudSerenityHotel.base.BaseController;
import com.cloudSerenityHotel.product.model.Categories;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.ProductService;
import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;



@RestController
@RequestMapping("/Product")
@CrossOrigin(origins = {"http://localhost:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductController {
    
	@Autowired
	private ProductServiceImpl productService;
	
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
	
	//新增
	@PostMapping("/insert")
	public int insert(@RequestBody Products products, @RequestBody Categories categories, @RequestBody ProductImages image, @RequestPart MultipartFile mf) throws IllegalStateException, IOException {
		
		//上傳圖片
		String fileName = mf.getOriginalFilename();
		String saveFileDir = "D:/hotel/workspace/SpringBootHotel/src/main/webapp/static/product/uploadImage/";
		
		File saveFilePath = new File(saveFileDir, fileName);
		mf.transferTo(saveFilePath);
		
        //把圖片路徑存到資料庫表格裡
        image.setImageUrl("/CloudSerenityHotel/static/product/uploadImage/" + fileName);
        productService.uploadImage(products,image);
		
		
		return productService.insertProduct(products,categories);
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
		String saveFileDir = "D:/hotel/workspace/SpringBootHotel/src/main/webapp/static/product/uploadImage/";
		
		File saveFilePath = new File(saveFileDir, fileName);
		mf.transferTo(saveFilePath);
		
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
