package com.cloudSerenityHotel.order.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudSerenityHotel.base.BaseController;
import com.cloudSerenityHotel.order.model.OrderBean;
import com.cloudSerenityHotel.order.model.OrderItemsBean;
import com.cloudSerenityHotel.order.service.impl.OrderServiceImpl;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;
// 測試
@CrossOrigin
@RestController // 變為JSON格式
@RequestMapping("/Order") // 設定這個 Controller 處理 /Order 開頭的請求
// 進入點URL -> http://localhost:8080/CloudSerenityHotel/Order/findAllOrders
public class OrderController extends BaseController {
	private static final long serialVersionUID = 1L;

	@Autowired
	private OrderServiceImpl orderServiceImpl;
	
	@Autowired
	private ProductServiceImpl productServiceImpl;

	// 查詢所有訂單_這是傳統的 Spring MVC 用法，返回 JSP 頁面
	@GetMapping("/findAllOrders")
	public String findAllOrders(Model model) {
		List<OrderBean> orders = orderServiceImpl.selectAll();
		model.addAttribute("orders", orders);
		return "/order/jsp/OrderList.jsp"; // 返回 JSP 頁面
	}

	// 查詢所有訂單_這是 RESTful API 用法，返回 JSON 資料
	/*
	 * @GetMapping("/findAllOrders") public ResponseEntity<List<OrderBean>>
	 * findAllOrders(){ List<OrderBean> orders = orderServiceImpl.selectAll(); if
	 * (orders.isEmpty()) { return
	 * ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 沒有訂單的情況下，返回 404 }
	 * return ResponseEntity.ok(orders); // 返回 200 OK 和訂單列表 }
	 */

	// 查詢單筆訂單
	@GetMapping("/findOrderById")
	public String findOrderById(@RequestParam int orderId, Model m) {
		// 查詢訂單
		OrderBean order = orderServiceImpl.selectOrderById(orderId);
		System.out.println("Order ID from order bean: " +order.getOrderId());
		
		// 查詢訂單細項
		List<OrderItemsBean> orderItems = orderServiceImpl.findOrderItemsByOrderId(orderId);
		
		// 將資料加入 model 以便在 JSP 中使用
		m.addAttribute("order", order);
		m.addAttribute("orderItems", orderItems);
		
		return "/order/jsp/OrderDetail.jsp";
		//return "redirect:/CloudSerenityHotel/Order/findAllOrders";
	}
	
	// 刪除單筆訂單
	@DeleteMapping("/{orderId}")
	public String deleteOrder(@PathVariable int orderId, RedirectAttributes redirectAttributes) {
	    boolean isDeleted = orderServiceImpl.deleteOrderById(orderId);
	    
	    if (isDeleted) {
	        redirectAttributes.addFlashAttribute("message", "訂單已成功刪除");
	    } else {
	        redirectAttributes.addFlashAttribute("message", "刪除訂單失敗");
	    }

	    return "redirect:/Order/findAllOrders"; // 刪除後返回訂單列表頁面
	}
	
	// 新增訂單
		// 封裝訂單
		@PostMapping("/add")
		public String createOrder(@ModelAttribute OrderBean order,
		                          @RequestParam List<Integer> productId, // 商品 ID 列表
		                          @RequestParam List<Integer> quantity,
		                          @RequestParam List<String> unitPrice,
		                          @RequestParam List<String> discount) {
			// 從數據庫加載商品實體
			List<Products> products = productServiceImpl.findProductsById(productId);
			
		    // 使用封裝好的方法來創建訂單細項
		    List<OrderItemsBean> orderItems = createOrderItems(products, quantity, unitPrice, discount);
	
		    // 計算訂單的總金額，並設置到訂單對象
		    BigDecimal finalAmount = orderServiceImpl.calculateOrderFinalAmount(orderItems);
		    order.setFinalAmount(finalAmount);
		    
		    // 插入訂單和訂單細項
		    orderServiceImpl.insertOrderWithItems(order, orderItems);
	
		    // 重定向到訂單詳情頁，並將訂單 ID 加入到 URL
		    return "redirect:/Order/findOrderById?orderId=" + order.getOrderId(); 
		}
	
		// 封裝訂單細項的創建邏輯，使用 BigDecimal
		private List<OrderItemsBean> createOrderItems(List<Products> products,
		                                              List<Integer> quantity,
		                                              List<String> unitPrice,
		                                              List<String> discount) {
		    List<OrderItemsBean> orderItems = new ArrayList<>();
		    for (int i = 0; i < products.size(); i++) {
		        OrderItemsBean item = new OrderItemsBean();
		        
		        // 設置關聯的商品
		        item.setProducts(products.get(i));
		        
		        // 設置數量、單價和折扣
		        item.setQuantity(quantity.get(i));
		        item.setUnitPrice(new BigDecimal(unitPrice.get(i)));  
		        item.setDiscount(new BigDecimal(discount.get(i)));    
	
		        // 調用 Service 計算小計
		        orderServiceImpl.calculateSubTotal(item);
	
		        orderItems.add(item);
		    }
		    return orderItems;
		}

		// 修改訂單
		@PostMapping("/update")
		public String updateOrder(@RequestParam int orderId,
		                          @ModelAttribute OrderBean order,
		                          Model model) {
		    // 確保傳回正確的訂單ID
		    order.setOrderId(orderId);  
		    
		    // 只有在更新時才設置修改日期，不修改創建日期
		    OrderBean existingOrder = orderServiceImpl.selectOrderById(orderId);
		    order.setOrderDate(existingOrder.getOrderDate());  // 保持創建日期不變
		    order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));  // 更新修改日期

		    // 調用服務層進行保存
		    orderServiceImpl.updateOrderById(order);

		    // 重新導向到訂單詳情頁
		    return "redirect:/Order/findOrderById?orderId=" + orderId;
		}


}
