package com.cloudSerenityHotel.order.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudSerenityHotel.base.BaseController;
import com.cloudSerenityHotel.order.dto.CartItemFrontendDTO;
import com.cloudSerenityHotel.order.dto.MemberForCartFrontendDTO;
import com.cloudSerenityHotel.order.service.impl.CartServiceImpl;

@CrossOrigin(origins = { "http://localhost:5173" }, // Vue 的本地開發環境域名
methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE } // 明確允許的請求方法
)
@RestController // 變為JSON格式
@RequestMapping("/Cart") // 設定這個 Controller 處理 /Cart 開頭的請求
//進入點URL -> http://localhost:8080/CloudSerenityHotel/Cart/findAllCartItems
public class CartController extends BaseController {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CartServiceImpl cartServiceImpl;

    // 查詢該用戶全部購物車內容
    @GetMapping("/findAllCartItems")
    public ResponseEntity<List<CartItemFrontendDTO>> getCartItems(@RequestParam Integer userId) {
        try {
            List<CartItemFrontendDTO> cartItems = cartServiceImpl.getCartItems(userId);
            return ResponseEntity.ok(cartItems);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
	
	// 加入商品到購物車
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {
        try {
            cartServiceImpl.addToCart(userId, productId, quantity);
            return ResponseEntity.ok("商品已成功加入購物車");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    // 修改購物車中的商品數量
    @PutMapping("/update")
    public ResponseEntity<CartItemFrontendDTO> updateCartItem(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam Integer newQuantity) {
        try {
        	System.out.println("Received userId: " + userId); // 打印 userId 以便調試
        	// 更新商品並返回更新後的 CartItemFrontendDTO
            CartItemFrontendDTO updatedItem = cartServiceImpl.updateCartItem(userId, productId, newQuantity);
            return ResponseEntity.ok(updatedItem); // 返回 DTO
        } catch (RuntimeException e) {
            CartItemFrontendDTO errorResponse = new CartItemFrontendDTO(newQuantity, newQuantity, "Error", "", 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
            errorResponse.setProductName(e.getMessage());  // 如果有錯誤，將錯誤訊息設定為商品名稱
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 移除購物車中的某個商品
    @DeleteMapping("/delete")
    public ResponseEntity<String> removeFromCart(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        try {
            cartServiceImpl.removeFromCart(userId, productId);
            return ResponseEntity.ok("商品已從購物車移除（假刪除）");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 清空購物車（假刪除）
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam Integer userId) {
        try {
            cartServiceImpl.clearCart(userId);
            return ResponseEntity.ok("購物車已清空（假刪除）");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    // 查詢會員資料並返回
    @GetMapping("/memberInfo")
    public ResponseEntity<MemberForCartFrontendDTO> getMemberInfo(@RequestParam Integer userId) {
        MemberForCartFrontendDTO memberDTO = cartServiceImpl.getMemberForCart(userId);
        
        if (memberDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.ok(memberDTO);
    }
}
