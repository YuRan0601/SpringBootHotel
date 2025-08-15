package com.cloudSerenityHotel.order.controller;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cloudSerenityHotel.base.BaseController;
import com.cloudSerenityHotel.order.dto.ApiResponseDTO;
import com.cloudSerenityHotel.order.dto.CartTurntoOrderDTO;
import com.cloudSerenityHotel.order.dto.OrderBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderFrontendDTO;
import com.cloudSerenityHotel.order.dto.PaymentDTO;
import com.cloudSerenityHotel.order.model.Order;
import com.cloudSerenityHotel.order.model.OrderItems;
import com.cloudSerenityHotel.order.service.EmailService;
import com.cloudSerenityHotel.order.service.OrderExportService;
import com.cloudSerenityHotel.order.service.OrderService;
import com.cloudSerenityHotel.order.service.PaymentService;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.ProductService;

//@CrossOrigin(origins = { "http://localhost:5173" }, // Vue 的本地開發環境域名
//		methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE } // 明確允許的請求方法
//)
@RestController // 變為JSON格式
@RequestMapping("/order") // 設定這個 Controller 處理 /Order 開頭的請求
// 進入點URL -> http://localhost:8080/CloudSerenityHotel/Order/findAllOrders
public class OrderController extends BaseController {
	private static final long serialVersionUID = 1L;

	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductService productService;
	@Autowired
    private PaymentService paymentService;
    @Autowired
    private EmailService emailService;
	@Autowired
    private OrderExportService orderExportService;

	// ===========================
    // 後台訂單管理
    // ===========================
	
	// 查所有訂單
	// @GetMapping("/findAllOrders")
	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<OrderBackendDTO>>> findAllOrders() {
		try {
			// 使用 Service 的方法直接返回 DTO
			List<OrderBackendDTO> orderDTOs = orderService.findAllOrders();
			return ResponseEntity.ok(new ApiResponseDTO<>(true, "查詢成功", orderDTOs));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
		}
	}

	// 查單筆訂單
	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponseDTO<OrderBackendDTO>> findOrderDetails(@PathVariable Integer orderId) {
	    try {
	    	OrderBackendDTO orderDTO = orderService.getOrderDetailsAsDTO(orderId);
	    	return ResponseEntity.ok(new ApiResponseDTO<>(true, "查詢成功", orderDTO));
	    } catch (RuntimeException  e) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

	// 查某狀態訂單_缺getOrdersByStatus方法
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponseDTO<List<OrderBackendDTO>>> getOrdersByStatus(@PathVariable String status) {
        try {
            List<OrderBackendDTO> orders = orderService.getOrdersByStatus(status);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "查詢成功", orders));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }
	
	/** 
     * 1️⃣ 純訂單（現金或非線上支付）
     * POST /orders
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<OrderBackendDTO>> createOrder(
    		@RequestBody CartTurntoOrderDTO dto) {
        try {
        	Order dbOrder = orderService.createOrderEntity(dto); // 拿到 Order 實體
            emailService.sendOrderCreatedEmail(dbOrder);         // 用實體寄信
            OrderBackendDTO responseDto = orderService.convertToBackendDTO(dbOrder); // 再轉 DTO
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO<>(true, "訂單新增成功", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 2️⃣ 信用卡付款（生成訂單 + 付款表單）
     * POST /orders/payment
     */
    @PostMapping("/payment")
    public ResponseEntity<ApiResponseDTO<String>> createOrderWithPayment(
            @RequestBody CartTurntoOrderDTO dto) {
        try {
            // Step 1: 先生成訂單
            OrderBackendDTO order = orderService.createOrder(dto);
            // Step 2: 生成付款表單
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setOrderId(order.getOrderId());
            paymentDTO.setFinalAmount(new BigDecimal(order.getFinalAmount()));
            paymentDTO.setProductName("CloudSerenity_Hotel伴手禮商城商品");
            paymentDTO.setPaymentMethod("Credit"); // 信用卡
            String paymentForm = paymentService.createPayment(paymentDTO);
            // 注意：寄信要等付款成功才寄，所以此處不寄信
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "訂單已生成，請完成支付", paymentForm));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 3️⃣ 金流回調
     * POST /orders/payment/return
     */
    @PostMapping("/payment/return")
    public ResponseEntity<String> paymentReturn(@RequestParam Map<String, String> allParams) {
        try {
            paymentService.processPaymentReturn(allParams);
            return ResponseEntity.ok("OK"); // 金流平台通常需要回傳 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
	// 更新訂單
	@PutMapping("/{orderId}")
	public ResponseEntity<ApiResponseDTO<OrderBackendDTO>> updateOrder(
			@PathVariable Integer orderId, @RequestBody OrderBackendDTO updatedOrderDTO) {
		try {
			Order updatedOrder = convertToEntity(updatedOrderDTO);
			OrderBackendDTO result = orderService.updateOrder(orderId, updatedOrder);
			return ResponseEntity.ok(new ApiResponseDTO<>(true, "更新成功", result));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponseDTO<>(false, e.getMessage(), null));
		}
	}
	
    // 刪除訂單
    @DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseDTO<String>> deleteOrder(@PathVariable int orderId) {
    	try {
            orderService.deleteOrderById(orderId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "刪除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

    // ===========================
    // 前台訂單查詢
    // ===========================

    // 查指定用戶所有訂單
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO<List<OrderFrontendDTO>>> getOrdersForUser(@PathVariable Integer userId) {
        try {
            List<OrderFrontendDTO> orders = orderService.getOrdersForFrontendByUserId(userId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "查詢成功", orders));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }
    
    // 查單筆指定用戶訂單
    @GetMapping("/user/{userId}/order/{orderId}")
    public ResponseEntity<ApiResponseDTO<OrderFrontendDTO>> getOrderDetailForUser(
            @PathVariable Integer userId,
            @PathVariable Integer orderId) {
        try {
            OrderFrontendDTO order = orderService.getOrderDetailForFrontend(userId, orderId);
            if (order != null) {
                return ResponseEntity.ok(new ApiResponseDTO<>(true, "訂單詳情查詢成功", order));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "查無 OrderId:" + orderId, null));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

    // 查指定用戶特定狀態訂單
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<ApiResponseDTO<List<OrderFrontendDTO>>> getOrdersByUserAndStatus(
            @PathVariable Integer userId,
            @PathVariable String status) {
        try {
            List<OrderFrontendDTO> orders = orderService.getOrdersByUserIdAndStatus(userId, status);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "查詢成功", orders));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }
    
    // ===========================
    // 私有輔助方法
    // ===========================
    private Order convertToEntity(OrderBackendDTO dto) {
        Order order = new Order();
        order.setOrderStatus(dto.getOrderStatus());
        order.setReceiveName(dto.getReceiveName());
        order.setEmail(dto.getEmail());
        order.setPhoneNumber(dto.getPhoneNumber());
        order.setAddress(dto.getAddress());
        // 其他不能修改的欄位不設置
        return order;
    }
    
}