package com.cloudSerenityHotel.order.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloudSerenityHotel.base.BaseController;
import com.cloudSerenityHotel.order.model.OrderBean;
import com.cloudSerenityHotel.order.model.OrderItemsBean;
import com.cloudSerenityHotel.order.service.impl.OrderServiceImpl;
import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = { "http://localhost:5173" }, // Vue 的本地開發環境域名
		methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE } // 明確允許的請求方法
)
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
	public ResponseEntity<?> findAllOrders() {
		List<OrderBean> orders = orderServiceImpl.selectAll();
		try {
			// 測試序列化輸出
			String json = new ObjectMapper().writeValueAsString(orders);
			System.out.println("序列化結果：" + json);
			return ResponseEntity.ok(orders);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("序列化失敗：" + e.getMessage());
		}
	}
	/*
	 * @GetMapping("/findAllOrders") public List<OrderBean> findAllOrders() { return
	 * orderServiceImpl.selectAll(); }
	 */
	/*
	 * public String findAllOrders(Model model) { List<OrderBean> orders =
	 * orderServiceImpl.selectAll(); model.addAttribute("orders", orders); return
	 * "/order/jsp/OrderList.jsp"; //返回 JSP 頁面 }
	 */

	// 查詢單筆訂單並初始化其細項和產品屬性
	@GetMapping("/findOrderDetails/{orderId}")
	public ResponseEntity<OrderBean> findOrderDetails(@PathVariable Integer orderId) {
		try {
			OrderBean order = orderServiceImpl.getOrderDetails(orderId);
			return ResponseEntity.ok(order);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// 查詢單筆訂單
	@GetMapping("/findOrderById/{orderId}")
	public ResponseEntity<OrderBean> findOrderById(@PathVariable int orderId) {
		try {
			OrderBean order = orderServiceImpl.selectOrderById(orderId);

			// 獲取並設置訂單細項
			List<OrderItemsBean> orderItems = orderServiceImpl.findOrderItemsByOrderId(orderId);
			order.setOrderItemsBeans(new HashSet<>(orderItems));

			return ResponseEntity.ok(order);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	/*
	 * @GetMapping("/findOrderById") public String findOrderById(@RequestParam int
	 * orderId, Model m) { // 查詢訂單 OrderBean order =
	 * orderServiceImpl.selectOrderById(orderId);
	 * System.out.println("Order ID from order bean: " +order.getOrderId());
	 * 
	 * // 查詢訂單細項 List<OrderItemsBean> orderItems =
	 * orderServiceImpl.findOrderItemsByOrderId(orderId);
	 * 
	 * // 將資料加入 model 以便在 JSP 中使用 m.addAttribute("order", order);
	 * m.addAttribute("orderItems", orderItems);
	 * 
	 * return "/order/jsp/OrderDetail.jsp"; //return
	 * "redirect:/CloudSerenityHotel/Order/findAllOrders"; }
	 */

	// 刪除單筆訂單
	@DeleteMapping("/delete/{orderId}")
	public ResponseEntity<String> deleteOrder(@PathVariable int orderId) {
		boolean isDeleted = orderServiceImpl.deleteOrderById(orderId);
		return isDeleted ? ResponseEntity.ok("刪除成功") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("刪除失敗：訂單不存在");
	}
	/*
	 * @DeleteMapping("/{orderId}") public String deleteOrder(@PathVariable int
	 * orderId, RedirectAttributes redirectAttributes) { boolean isDeleted =
	 * orderServiceImpl.deleteOrderById(orderId);
	 * 
	 * if (isDeleted) { redirectAttributes.addFlashAttribute("message", "訂單已成功刪除");
	 * } else { redirectAttributes.addFlashAttribute("message", "刪除訂單失敗"); }
	 * 
	 * return "redirect:/Order/findAllOrders"; // 刪除後返回訂單列表頁面 }
	 */

	// 新增訂單
	// @PostMapping("/add") // 待改
	/*
	 * public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderBean
	 * order) { try { List<OrderItemsBean> orderItems = new
	 * ArrayList<>(order.getOrderItemsBeans()); for (OrderItemsBean item :
	 * orderItems) { Products product =
	 * productServiceImpl.findProductsById(List.of(item.getProducts().getProductId()
	 * )) .get(0); item.setProducts(product);
	 * orderServiceImpl.calculateSubTotal(item); }
	 * 
	 * BigDecimal finalAmount =
	 * orderServiceImpl.calculateOrderFinalAmount(orderItems);
	 * order.setFinalAmount(finalAmount); order.setOrderItemsBeans(new
	 * HashSet<>(orderItems)); orderServiceImpl.insertOrderWithItems(order,
	 * orderItems);
	 * 
	 * Map<String, Object> response = new HashMap<>(); response.put("success",
	 * true); response.put("message", "訂單新增成功"); response.put("orderId",
	 * order.getOrderId());
	 * 
	 * return ResponseEntity.ok(response); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body(Map.of("success", false, "message", "訂單新增失敗：" + e.getMessage())); } }
	 */

	/*
	 * // 封裝訂單
	 * 
	 * @PostMapping("/add") public String createOrder(@ModelAttribute OrderBean
	 * order,
	 * 
	 * @RequestParam List<Integer> productId, // 商品 ID 列表
	 * 
	 * @RequestParam List<Integer> quantity,
	 * 
	 * @RequestParam List<String> unitPrice,
	 * 
	 * @RequestParam List<String> discount) { // 從數據庫加載商品實體 List<Products> products
	 * = productServiceImpl.findProductsById(productId);
	 * 
	 * // 使用封裝好的方法來創建訂單細項 List<OrderItemsBean> orderItems =
	 * createOrderItems(products, quantity, unitPrice, discount);
	 * 
	 * // 計算訂單的總金額，並設置到訂單對象 BigDecimal finalAmount =
	 * orderServiceImpl.calculateOrderFinalAmount(orderItems);
	 * order.setFinalAmount(finalAmount);
	 * 
	 * // 插入訂單和訂單細項 orderServiceImpl.insertOrderWithItems(order, orderItems);
	 * 
	 * // 重定向到訂單詳情頁，並將訂單 ID 加入到 URL return "redirect:/Order/findOrderById?orderId="
	 * + order.getOrderId(); }
	 * 
	 * // 封裝訂單細項的創建邏輯，使用 BigDecimal private List<OrderItemsBean>
	 * createOrderItems(List<Products> products, List<Integer> quantity,
	 * List<String> unitPrice, List<String> discount) { List<OrderItemsBean>
	 * orderItems = new ArrayList<>(); for (int i = 0; i < products.size(); i++) {
	 * OrderItemsBean item = new OrderItemsBean();
	 * 
	 * // 設置關聯的商品 item.setProducts(products.get(i));
	 * 
	 * // 設置數量、單價和折扣 item.setQuantity(quantity.get(i)); item.setUnitPrice(new
	 * BigDecimal(unitPrice.get(i))); item.setDiscount(new
	 * BigDecimal(discount.get(i)));
	 * 
	 * // 調用 Service 計算小計 orderServiceImpl.calculateSubTotal(item);
	 * 
	 * orderItems.add(item); } return orderItems; }
	 */

	// 查詢修改訂單
	@GetMapping("/getUpdateOrderById/{orderId}")
	public ResponseEntity<OrderBean> getUpdateOrderById(@PathVariable Integer orderId) {
		System.out.println("接收到請求，orderId: " + orderId);
		try {
			OrderBean order = orderServiceImpl.selectOrderById(orderId);
			if (order == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			List<OrderItemsBean> orderItems = orderServiceImpl.findOrderItemsByOrderId(orderId);
			order.setOrderItemsBeans(new HashSet<>(orderItems));
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// 修改訂單
	@PutMapping("/update/{orderId}")
	public ResponseEntity<String> updateOrder(@PathVariable int orderId, @RequestBody OrderBean order) {
		try {
			// 查詢訂單是否存在
			OrderBean existingOrder = orderServiceImpl.selectOrderById(orderId);
			if (existingOrder == null) {
				// 如果訂單不存在，回傳 404
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("修改失敗：訂單不存在");
			}
			// 更新資料
			order.setOrderId(orderId);
			order.setOrderDate(existingOrder.getOrderDate()); // 保留原訂單日期
			order.setUpdatedAt(new Timestamp(System.currentTimeMillis())); // 設定更新時間

			// 如果有細項需要同步更新
			if (order.getOrderItemsBeans() != null && !order.getOrderItemsBeans().isEmpty()) {
				List<OrderItemsBean> orderItems = new ArrayList<>(order.getOrderItemsBeans());

				// 更新訂單細項
				orderServiceImpl.updateOrderItems(orderId, orderItems);

				// 重新計算訂單總金額
				orderServiceImpl.calculateOrderTotal(order, orderItems);
			}

			// 保存訂單主檔（包含新的總金額）
			orderServiceImpl.updateOrderById(order);

			return ResponseEntity.ok("修改訂單成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("修改訂單失敗：" + e.getMessage());
		}
	}
	/*
	 * @PostMapping("/update") public String updateOrder(@PathVariable int
	 * orderId, @RequestBody OrderBean order) { // 確保傳回正確的訂單ID
	 * order.setOrderId(orderId);
	 * 
	 * // 只有在更新時才設置修改日期，不修改創建日期 OrderBean existingOrder =
	 * orderServiceImpl.selectOrderById(orderId);
	 * order.setOrderDate(existingOrder.getOrderDate()); // 保持創建日期不變
	 * order.setUpdatedAt(new Timestamp(System.currentTimeMillis())); // 更新修改日期
	 * 
	 * // 調用服務層進行保存 orderServiceImpl.updateOrderById(order);
	 * 
	 * return "修改訂單成功"; }
	 */
}
