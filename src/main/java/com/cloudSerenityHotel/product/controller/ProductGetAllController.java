package com.cloudSerenityHotel.product.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.ProductService;
import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;



@WebServlet("/ProductGetAllController")
public class ProductGetAllController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    ProductService productService = new ProductServiceImpl();
    
    public ProductGetAllController() {
        super();
    }

    //查詢全部
	protected void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("allProducts", productService.selectAllProduct());
		request.getRequestDispatcher("/static/product/ProductAllList.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
    
}
