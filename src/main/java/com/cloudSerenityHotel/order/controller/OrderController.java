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
import com.cloudSerenityHotel.order.dto.ApiResponse;
import com.cloudSerenityHotel.order.dto.CartTurntoOrderDTO;
import com.cloudSerenityHotel.order.dto.OrderBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderFrontendDTO;
import com.cloudSerenityHotel.order.dto.PaymentDTO;
import com.cloudSerenityHotel.order.model.Order;
import com.cloudSerenityHotel.order.model.OrderItems;
import com.cloudSerenityHotel.order.service.OrderExportService;
import com.cloudSerenityHotel.order.service.PaymentService;
import com.cloudSerenityHotel.order.service.impl.OrderServiceImpl;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;

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
	
	@Autowired
    private PaymentService paymentService;  // 注入 PaymentService
	
	@Autowired
    private OrderExportService orderExportService;

	/*@Autowired
    private CartServiceImpl cartServiceImpl;  // 注入 CartService*/

// 後台
	// 查詢所有訂單，返回 DTO 列表
	@GetMapping("/findAllOrders")
	public ResponseEntity<List<OrderBackendDTO>> findAllOrders() {
		try {
			// 使用 Service 的方法直接返回 DTO
			List<OrderBackendDTO> orderDTOs = orderServiceImpl.findAllOrders();
			return ResponseEntity.ok(orderDTOs);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	// 分頁_未使用,交由前端顯示分頁
	@GetMapping("/paged")
    public ResponseEntity<Page<OrderBackendDTO>> getOrdersWithPagination(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        Page<OrderBackendDTO> orderPage = orderServiceImpl.findOrdersWithPagination(page, size);
        System.out.println("分頁內容: " + orderPage.getContent());
        return ResponseEntity.ok(orderPage);
    }

	// 查詢單筆訂單，返回 DTO
	@GetMapping("/findOrderDetails/{orderId}")
	public ResponseEntity<ApiResponse<OrderBackendDTO>> findOrderDetails(@PathVariable Integer orderId) {
	    try {
	        // 查詢訂單並轉換為 DTO
	    	OrderBackendDTO orderDTO = orderServiceImpl.getOrderDetailsAsDTO(orderId);

	        // 創建成功的 ApiResponse
	        ApiResponse<OrderBackendDTO> response = new ApiResponse<>(true, "查詢成功", orderDTO);

	        // 返回成功響應
	        return ResponseEntity.ok(response);
	    } catch (NoSuchElementException e) {
	        // 查無此訂單，返回 404 和錯誤訊息
	        ApiResponse<OrderBackendDTO> errorResponse = new ApiResponse<>(false, "查無 OrderId:" + orderId + " 訂單，請確認後重試！", null);

	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	    } catch (Exception e) {
	        // 其他例外，返回 500 和錯誤訊息
	        ApiResponse<OrderBackendDTO> errorResponse = new ApiResponse<>(false, "伺服器發生錯誤，請稍後再試！", null);

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}

	// 刪除單筆訂單
	@DeleteMapping("/delete/{orderId}")
	public ResponseEntity<String> deleteOrder(@PathVariable int orderId) {
		boolean isDeleted = orderServiceImpl.deleteOrderById(orderId);
		return isDeleted ? ResponseEntity.ok("刪除成功") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("刪除失敗：訂單不存在");
	}

	@PostMapping("/add")
	public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Order order) {
	    try {
	        // 檢查訂單細項是否存在
	        if (order.getOrderItemsBeans() == null || order.getOrderItemsBeans().isEmpty()) {
	            return ResponseEntity.badRequest().body(Map.of(
	                    "success", false,
	                    "message", "訂單細項不能為空！"
	            ));
	        }

	        // 為訂單細項加載產品資訊
	        for (OrderItems item : order.getOrderItemsBeans()) {
	            if (item.getProducts() == null || item.getProducts().getProductId() == null) {
	                return ResponseEntity.badRequest().body(Map.of(
	                        "success", false,
	                        "message", "產品資訊未正確設置！"
	                ));
	            }

	            Products product = productServiceImpl
	                    .findProductsById(List.of(item.getProducts().getProductId()))
	                    .stream()
	                    .findFirst()
	                    .orElse(null);

	            if (product == null) {
	                return ResponseEntity.badRequest().body(Map.of(
	                        "success", false,
	                        "message", "產品 ID：" + item.getProducts().getProductId() + " 不存在！"
	                ));
	            }

	            item.setProducts(product); // 加載產品信息
	        }

	        // 計算總金額，新增訂單和細項
	        OrderBackendDTO createdOrder = orderServiceImpl.insertOrderWithItems(order,
	                new ArrayList<>(order.getOrderItemsBeans()));

	        // 返回成功訊息，包含訂單 ID 和詳細數據
	        return ResponseEntity.ok(Map.of(
	                "success", true,
	                "message", "訂單新增成功",
	                "orderId", createdOrder.getOrderId(),
	                "orderDetails", createdOrder // 可返回更多訂單細節
	        ));
	    } catch (Exception e) {
	        e.printStackTrace();
	        // 返回錯誤訊息，附加詳細的例外信息
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of(
	                        "success", false,
	                        "message", "訂單新增失敗：" + e.getMessage(),
	                        "error", e.getClass().getName()
	                ));
	    }
	}



	// 查詢訂單以修改
	@GetMapping("/getUpdateOrderById/{orderId}")
	public ResponseEntity<OrderBackendDTO> getUpdateOrderById(@PathVariable Integer orderId) {
		try {
			OrderBackendDTO orderDTO = orderServiceImpl.getOrderDetailsAsDTO(orderId);
			return ResponseEntity.ok(orderDTO);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// 更新訂單
    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderBackendDTO> updateOrder(
            @PathVariable Integer orderId, // 獲取 orderId
            @RequestBody OrderBackendDTO updatedOrderDTO) { // 接收前端傳遞的更新數據（OrderDTO）
        try {
            // 轉換 DTO 為實體，並更新訂單
            Order updatedOrder = convertToEntity(updatedOrderDTO);
            OrderBackendDTO result = orderServiceImpl.updateOrder(orderId, updatedOrder);

            // 返回更新後的訂單資料
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            // 處理異常情況
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 將 DTO 轉換為實體的方法
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
    
    // 後台匯出訂單
    @PostMapping("/exportOrders")
    public ResponseEntity<String> exportOrders(@RequestParam String orderStatus, @RequestParam String format, @RequestParam String filePath) {
        try {
            // 解碼 URL 編碼的 filePath
            String decodedFilePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8.name());

            // 清理換行符或其他不必要的字符
            decodedFilePath = decodedFilePath.replaceAll("[\r\n]", ""); // 清理換行符

            // 處理反斜線，將反斜線替換為正斜線
            decodedFilePath = decodedFilePath.replace("\\", "/");

            // 確認 filePath 是否正確
            System.out.println("Decoded file path: " + decodedFilePath);

            // 查詢符合條件的訂單
            List<OrderBackendDTO> orders = orderServiceImpl.findAllOrders().stream()
                    .filter(order -> orderStatus.equals(order.getOrderStatus()))  // 根據狀態篩選
                    .collect(Collectors.toList());

            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("無符合條件的訂單");
            }

            // 匯出訂單資料，並根據格式選擇匯出方式
            orderExportService.exportOrdersByStatus(orders, orderStatus, format, decodedFilePath);
            return ResponseEntity.ok("匯出成功");
        } catch (Exception e) {
            e.printStackTrace();  // 打印具體錯誤
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("匯出失敗: " + e.getMessage());
        }
    }

    
// 前台
	// 查詢指定用戶的所有訂單（包含訂單細項）
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderFrontendDTO>> getOrdersForFrontendByUserId(@PathVariable Integer userId) {
        try {
            // 調用 Service 的方法，獲取該用戶的訂單列表
            List<OrderFrontendDTO> orders = orderServiceImpl.getOrdersForFrontendByUserId(userId);

            // 返回成功響應
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            // 返回伺服器錯誤響應
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // 查詢指定用戶的特定訂單（包含訂單細項）
    @GetMapping("/user/{userId}/order/{orderId}")
    public ResponseEntity<ApiResponse<OrderFrontendDTO>> getOrderDetailForFrontend(
            @PathVariable Integer userId, 
            @PathVariable Integer orderId) {
        try {
            // 調用 Service 層的查詢方法
            OrderFrontendDTO order = orderServiceImpl.getOrderDetailForFrontend(userId, orderId);

            // 如果訂單存在，返回成功響應
            if (order != null) {
                ApiResponse<OrderFrontendDTO> response = new ApiResponse<>(
                    true, 
                    "訂單詳情查詢成功", 
                    order
                );
                return ResponseEntity.ok(response); // 返回成功響應
            } else {
                // 如果沒有找到該訂單，返回 404 錯誤，並且返回前台訂單的 API 回應
                ApiResponse<OrderFrontendDTO> errorResponse = new ApiResponse<>(
                    false, 
                    "查無 OrderId:" + orderId + " 訂單，請確認後重試！", 
                    null
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // 返回錯誤訊息
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 返回伺服器錯誤響應，並將錯誤訊息封裝進 ApiResponse 中
            ApiResponse<OrderFrontendDTO> errorResponse = new ApiResponse<>(
                false, 
                "無法處理訂單請求，請稍後再試", 
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    // Cart -> Order
    // 無串金流版本
    @PostMapping("/CartToOrder")
    public ResponseEntity<OrderBackendDTO> createOrder(@RequestBody CartTurntoOrderDTO cartTurntoOrderDTO) {
        try {
            // 呼叫服務方法來創建訂單
        	OrderBackendDTO createdOrder = orderServiceImpl.createOrder(cartTurntoOrderDTO);

            // 回傳成功的狀態碼與創建的訂單物件
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // 捕捉異常並返回錯誤訊息
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    // 串金流版本_ngrok http http://localhost:8080
    @PostMapping("/CartToOrderWithPayment")
    public ResponseEntity<String> createOrderWithPayment(@RequestBody CartTurntoOrderDTO cartTurntoOrderDTO) {
        try {
            // 1. 創建訂單
            OrderBackendDTO createdOrder = orderServiceImpl.createOrder(cartTurntoOrderDTO);

            // 2. 生成 PaymentDTO 並進行支付處理
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setOrderId(createdOrder.getOrderId());
            paymentDTO.setFinalAmount(new BigDecimal(createdOrder.getFinalAmount()));
            paymentDTO.setProductName("購物車商品");
            paymentDTO.setPaymentMethod("Credit");

            // 3. 返回支付表單，前端提交支付請求
            String paymentForm = paymentService.createPayment(paymentDTO);

            // 4. 返回支付表單給前端
            return ResponseEntity.ok(paymentForm);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>("訂單創建或支付處理失敗", HttpStatus.BAD_REQUEST);
        }
    }
    
    // 處理支付結果回調
    @PostMapping("/paymentResult")
    public String paymentReturn(@RequestParam Map<String, String> responseParams) {
    	// 印出回傳參數
        System.out.println("綠界回傳的支付結果參數：");
        for (Map.Entry<String, String> entry : responseParams.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        try {
            // 調用 PaymentService 處理支付回調
            paymentService.processPaymentReturn(responseParams);
            return "交易成功";
        } catch (RuntimeException e) {
        	e.printStackTrace();
            return "交易失敗: " + e.getMessage();
        }
    }
}