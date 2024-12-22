package com.cloudSerenityHotel.product.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.math.BigDecimal;

import com.cloudSerenityHotel.base.BaseController;
import com.cloudSerenityHotel.product.model.Categories;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.ProductService;
import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;


@WebServlet("/product/*")
@MultipartConfig/*(location="D:/servlet/workspace/CloudSerenityHotel/src/main/webapp/static/product/uploadImage/")*/
public class ProductController extends BaseController {
	private static final long serialVersionUID = 1L;
	
    ProductService productService = new ProductServiceImpl();
    
    public ProductController() {
        super();
    }

    
    //查詢單筆
	protected void select(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer products = Integer.parseInt(request.getParameter("product_id"));
		request.setAttribute("products", productService.selectProduct(products));
		request.getRequestDispatcher("/static/product/ProductList.jsp").forward(request, response);
	}
	
	
	//新增
	protected void insert(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {
		//設定商品主表上的欄位
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		BigDecimal price = new BigDecimal(request.getParameter("price"));
		BigDecimal special_price = new BigDecimal(request.getParameter("special_price"));
		
		Products products = new Products();
		products.setName(name);
		products.setDescription(description);
		products.setPrice(price);
		products.setSpecial_price(special_price);
		
		//分類
		String categoriesName = request.getParameter("categoriesName");
		Categories categories = new Categories();
		categories.setName(categoriesName);
		
		productService.insertProduct(products,categories);
		
		//上傳圖片
	    for (Part part : request.getParts()) {
	        String filename = part.getSubmittedFileName();
	        
	        //不知道為什麼如果只點選一張，會出現好幾個這樣的/upload/null路徑
	        if (filename == null || filename.isEmpty()) {
	            continue; // 跳過無效文件
	        }
	        //上傳到專案路徑裡
	        part.write("D:/hotel/workspace/CloudSerenityHotel/src/main/webapp/static/product/uploadImage/" + filename);
	        
	        //把圖片路徑存到資料庫表格裡
	        ProductImages image = new ProductImages();
//	        image.setProduct_id(newProductId);
	        image.setImage_url("/CloudSerenityHotel/static/product/uploadImage/" + filename);
	        productService.uploadImage(products,image);
	    }

		request.setAttribute("allProducts", productService.selectAllProduct());
		request.getRequestDispatcher("/static/product/ProductAllList.jsp").forward(request, response);
	}
	
	//刪除
	protected void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer products = Integer.parseInt(request.getParameter("product_id"));
		request.setAttribute("products", productService.deleteProduct(products));
		request.getRequestDispatcher("/ProductGetAllController").forward(request, response);
	}
	
	//取的某商品資料 顯示在修改頁面
	protected void GetupdateData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer products = Integer.parseInt(request.getParameter("product_id"));
		request.setAttribute("products", productService.selectProduct(products));
		request.getRequestDispatcher("/static/product/ProductLaunchedData.jsp").forward(request, response);
	}
	
	
	//修改
	protected void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//設定商品主表上的欄位
		Integer productId = Integer.parseInt(request.getParameter("product_id"));
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		BigDecimal price = new BigDecimal(request.getParameter("price"));
		BigDecimal special_price = new BigDecimal(request.getParameter("special_price"));
		
		Products products = new Products();
		products.setProduct_id(productId);
		products.setName(name);
		products.setDescription(description);
		products.setPrice(price);
		products.setSpecial_price(special_price);
		
		//分類 (還會需修改 沒做很好)
		String categoriesName = request.getParameter("categoriesName");
		Categories categories = new Categories();
		categories.setName(categoriesName);
		
		productService.updateProduct(products,categories);
		
		//上傳圖片 (還會需修改 沒做很好)
	    for (Part part : request.getParts()) {
	        String filename = part.getSubmittedFileName();
	        
	        //不知道為什麼如果只點選一張，會出現好幾個這樣的/upload/null路徑
	        if (filename == null || filename.isEmpty()) {
	            continue; // 跳過無效文件
	        }
	        //上傳到專案路徑裡
	        part.write("D:/hotel/workspace/CloudSerenityHotel/src/main/webapp/static/product/uploadImage/" + filename);
	        
	        //把圖片路徑存到資料庫表格裡
	        ProductImages image = new ProductImages();
	        image.setImage_url("/CloudSerenityHotel/static/product/uploadImage/" + filename);
	        productService.uploadImage(products,image);
	    }
	    request.setAttribute("allProducts", productService.selectAllProduct());
		request.getRequestDispatcher("/ProductGetAllController").forward(request, response);
	}
	
	
}
