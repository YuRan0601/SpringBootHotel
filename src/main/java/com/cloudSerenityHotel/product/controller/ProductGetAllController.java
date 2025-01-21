package com.cloudSerenityHotel.product.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;



@RestController
@RequestMapping("/Product")
@CrossOrigin(origins = {"http://localhost:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductGetAllController {
 
 @Autowired
 private ProductServiceImpl productService;
 
 //查詢全部
 @GetMapping("/selectAll")
 public List<Map<String, Object>> selectAll() {
  return productService.selectAllProduct();
 }
    
}
