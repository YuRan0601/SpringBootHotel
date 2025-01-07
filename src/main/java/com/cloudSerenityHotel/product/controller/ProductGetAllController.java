package com.cloudSerenityHotel.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;



@Controller
public class ProductGetAllController {
	
	@Autowired
	private ProductServiceImpl productService;
	
	//查詢全部
	@GetMapping("/selectAll")
	public String selectAll(Model m) {
		m.addAttribute("allProducts",productService.selectAllProduct());
		return "/product/ProductAllList.jsp";
	}
    
}
