package com.cloudSerenityHotel.order.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cloudSerenityHotel.base.BaseController;
import com.cloudSerenityHotel.order.dto.ApiResponseDTO;
import com.cloudSerenityHotel.order.dto.CartItemFrontendDTO;
import com.cloudSerenityHotel.order.dto.MemberForCartFrontendDTO;
import com.cloudSerenityHotel.order.service.CartService;

//@CrossOrigin(origins = { "http://localhost:5173" }, // Vue 的本地開發環境域名
//methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE } // 明確允許的請求方法
//)
//進入點URL -> http://localhost:8080/CloudSerenityHotel/cart/items
@RestController // 變為JSON格式
@RequestMapping("/cart") // 小寫、統一資源名稱
public class CartController extends BaseController {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CartService cartService;

	/**
     * 查詢該用戶全部購物車內容
     * GET /cart/items?userId=1
     */
    @GetMapping("/items")
    public ResponseEntity<ApiResponseDTO<List<CartItemFrontendDTO>>> getCartItems(@RequestParam Integer userId) {
        try {
            List<CartItemFrontendDTO> cartItems = cartService.getCartItems(userId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "取得購物車成功", cartItems));
        } catch (RuntimeException e) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }
	
    /**
     * 加入商品到購物車
     * POST /cart/items
     * body: { "userId": 1, "productId": 2, "quantity": 3 }
     */
    @PostMapping("/items")
    public ResponseEntity<ApiResponseDTO<CartItemFrontendDTO>> addToCart(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {
        try {
        	// service 新增後回傳該購物車項目的 DTO
        	CartItemFrontendDTO newCartItem=cartService.addToCart(userId, productId, quantity);
            return ResponseEntity.ok(
                    new ApiResponseDTO<>(true, "商品已成功加入購物車", newCartItem));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 修改購物車中商品數量
     * PUT /cart/items/{productId}?userId=1
     */
    @PutMapping("/items/{productId}")
    public ResponseEntity<ApiResponseDTO<CartItemFrontendDTO>> updateCartItem(
    		@PathVariable Integer productId,
            @RequestParam Integer userId,
            @RequestParam Integer newQuantity) {
        try {
        	System.out.println("Received userId: " + userId); // 打印 userId 以便調試
        	// 更新商品並返回更新後的 CartItemFrontendDTO
            CartItemFrontendDTO updatedItem = cartService.updateCartItem(userId, productId, newQuantity);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "更新成功", updatedItem));
        } catch (RuntimeException e) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

    /**
     * 移除購物車中的某個商品
     * DELETE /cart/items/{productId}?userId=1
     */
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponseDTO<Void>> removeFromCart(
    		@PathVariable Integer productId,
            @RequestParam Integer userId) {
        try {
            cartService.removeFromCart(userId, productId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "商品已從購物車移除（假刪除）", null));
        } catch (RuntimeException e) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }

    /**
     * 清空購物車
     * DELETE /cart/items?userId=1
     */
    @DeleteMapping("/items")
    public ResponseEntity<ApiResponseDTO<Void>> clearCart(@RequestParam Integer userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "購物車已清空（假刪除）", null));
        } catch (RuntimeException e) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }
    
    /**
     * 查詢會員資料
     * GET /cart/member?userId=1
     */
    @GetMapping("/member")
    public ResponseEntity<ApiResponseDTO<MemberForCartFrontendDTO>> getMemberInfo(@RequestParam Integer userId) {
    	try {
            MemberForCartFrontendDTO memberDTO = cartService.getMemberForCart(userId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "取得會員資料成功", memberDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        }
    }
    
}
