package com.cloudSerenityHotel.order.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
import com.cloudSerenityHotel.order.dto.OrderBackendDTO;
import com.cloudSerenityHotel.order.model.OrderBean;
import com.cloudSerenityHotel.order.model.OrderItemsBean;
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
	public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderBean order) {
	    try {
	        // 檢查訂單細項是否存在
	        if (order.getOrderItemsBeans() == null || order.getOrderItemsBeans().isEmpty()) {
	            return ResponseEntity.badRequest().body(Map.of(
	                    "success", false,
	                    "message", "訂單細項不能為空！"
	            ));
	        }

	        // 為訂單細項加載產品資訊
	        for (OrderItemsBean item : order.getOrderItemsBeans()) {
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
            OrderBean updatedOrder = convertToEntity(updatedOrderDTO);
            OrderBackendDTO result = orderServiceImpl.updateOrder(orderId, updatedOrder);

            // 返回更新後的訂單資料
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            // 處理異常情況
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 將 DTO 轉換為實體的方法
    private OrderBean convertToEntity(OrderBackendDTO dto) {
        OrderBean order = new OrderBean();
        order.setOrderStatus(dto.getOrderStatus());
        order.setReceiveName(dto.getReceiveName());
        order.setEmail(dto.getEmail());
        order.setPhoneNumber(dto.getPhoneNumber());
        order.setAddress(dto.getAddress());
        // 其他不能修改的欄位不設置
        return order;
    }

}
