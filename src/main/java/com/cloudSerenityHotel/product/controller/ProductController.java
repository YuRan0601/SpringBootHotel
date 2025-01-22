package com.cloudSerenityHotel.product.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cloudSerenityHotel.product.model.Categories;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;

@Controller
public class ProductController {
    
	@Autowired
	private ProductServiceImpl productService;
	
	//側邊選單(套版用)
	@GetMapping("/aside")
	public String productHome() {
		return "/common/aside.html";
	}
	
	//查詢單筆
	@GetMapping("/select")
	public String select(@RequestParam int productId, Model m) {
		m.addAttribute("products",productService.selectProduct(productId));
		return "/product/ProductList.jsp";
	}
	
	//新增
	@PostMapping("/insert")
	public String insert(@RequestParam String name, @RequestParam String description, @RequestParam BigDecimal price,
						@RequestParam BigDecimal specialPrice,@RequestParam String categoriesName, @RequestParam("file") MultipartFile mf, Model m) throws IllegalStateException, IOException {
		
		//設定商品主表上的欄位
		Products products = new Products();
		products.setName(name);
		products.setDescription(description);
		products.setPrice(price);
		products.setSpecialPrice(specialPrice);
		
		//分類
		Categories categories = new Categories();
		categories.setName(categoriesName);
		
		productService.insertProduct(products,categories);
		
		//上傳圖片
		String fileName = mf.getOriginalFilename();
		String saveFileDir = "src/main/webapp/static/product/uploadImage/";
		
		File saveFilePath = new File(saveFileDir, fileName);
		mf.transferTo(saveFilePath.getAbsoluteFile());
		
        //把圖片路徑存到資料庫表格裡
        ProductImages image = new ProductImages();
        image.setImageUrl("/CloudSerenityHotel/static/product/uploadImage/" + fileName);
        productService.uploadImage(products,image);
		
		
//		m.addAttribute("allProducts", productService.selectAllProduct());
		
		return "redirect:/selectAll";
	}
	
	//刪除
	@PostMapping("/delete/{productId}")
	public String delete(@PathVariable int productId, Model m) {
		m.addAttribute("products",productService.deleteProduct(productId));
		return "redirect:/selectAll";
	}
	
	//取的某商品資料 顯示在修改頁面
	@PostMapping("/getUpdate/{productId}")
	public String getUpdate(@PathVariable int productId, Model m) {
		m.addAttribute("products", productService.selectProduct(productId));
		return "/product/ProductLaunchedData.jsp";
	}
	
	//修改
	@PostMapping("/update/{productId}")
	public String update(@PathVariable int productId, @RequestParam String name, @RequestParam String description, @RequestParam BigDecimal price,
			@RequestParam BigDecimal specialPrice,@RequestParam String categoriesName, @RequestParam("file") MultipartFile mf, Model m) throws IllegalStateException, IOException {
		
		//設定商品主表上的欄位
		Products products = new Products();
		products.setProductId(productId);
		products.setName(name);
		products.setDescription(description);
		products.setPrice(price);
		products.setSpecialPrice(specialPrice);
		
		//分類
		Categories categories = new Categories();
		categories.setName(categoriesName);
		
		productService.updateProduct(products,categories);
		
		//上傳圖片
		String fileName = mf.getOriginalFilename();
		String saveFileDir = "src/main/webapp/static/product/uploadImage/";
		
		File saveFilePath = new File(saveFileDir, fileName);
		mf.transferTo(saveFilePath.getAbsoluteFile());
		
        //把圖片路徑存到資料庫表格裡
        ProductImages image = new ProductImages();
        image.setImageUrl("/CloudSerenityHotel/static/product/uploadImage/" + fileName);
        productService.uploadImage(products,image);
		
		m.addAttribute("allProducts", productService.selectAllProduct());
		return "redirect:/selectAll";
	}
}
