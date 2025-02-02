package com.cloudSerenityHotel.order.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloudSerenityHotel.order.dao.CartDao;
import com.cloudSerenityHotel.order.dao.CartItemsDao;
import com.cloudSerenityHotel.order.dto.CartItemFrontendDTO;
import com.cloudSerenityHotel.order.model.Cart;
import com.cloudSerenityHotel.order.model.CartItems;
import com.cloudSerenityHotel.order.service.CartService;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.product.service.impl.ProductServiceImpl;

import jakarta.transaction.Transactional;

//JPA 的 save 方法會根據物件是否有主鍵來自動選擇是執行「新增」還是「更新」。
@Service
@Transactional // 自動交易管理員
public class CartServiceImpl implements CartService{

	@Autowired
	private CartDao cartDao;
	
	@Autowired
	private CartItemsDao cartItemsDao;
	
	@Autowired
	private ProductServiceImpl productService;
	
	// 將 CartItems 實體轉換為前台 DTO
    public CartItemFrontendDTO convertToFrontendDTO(CartItems cartItem) {
    	 // 獲取商品圖片列表
        List<ProductImages> productImages = cartItem.getProducts().getProductImages();

        // 判斷列表是否為空，若非空則取第一張圖片，否則設為默認圖片或空字串
        String imageUrl = productImages != null && !productImages.isEmpty()
                ? productImages.get(0).getImageUrl() // 獲取第一張圖片的 URL
                : ""; // 默認值，當無圖片時

        return new CartItemFrontendDTO(
        		cartItem.getCartItemId(), // 確保返回 cartItemId
        		cartItem.getProducts().getProductId(), // 添加 productId
                cartItem.getProducts().getProductName(), // 商品名稱
                imageUrl, // 商品圖片 URL
                cartItem.getQuantity(),
                cartItem.getUnitPrice(),
                cartItem.getDiscount(),
                cartItem.getSubtotal()
            );
    }
    
    // 創建新的購物車
    private Cart createNewCart(Integer userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartDao.save(cart);
    }
    
    // 新增新的 CartItem，不影響舊記錄
    private void createNewCartItem(Cart cart, Integer productId, int quantity) {
        Products product = productService.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 計算單價與折扣
        BigDecimal unitPrice = product.getPrice(); // 使用商品原價作為單價
        BigDecimal discount = product.getSpecialPrice() != null && product.getSpecialPrice().compareTo(BigDecimal.ZERO) > 0
                ? product.getPrice().subtract(product.getSpecialPrice()) // 折扣 = 原價 - 特價
                : BigDecimal.ZERO; // 如果沒有特價，折扣為 0
        BigDecimal subtotal = unitPrice.subtract(discount).multiply(BigDecimal.valueOf(quantity)); // 小計 = (單價 - 折扣) * 數量

        // 創建新的購物車項目
        CartItems newCartItem = new CartItems();
        newCartItem.setCart(cart);
        newCartItem.setProducts(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setUnitPrice(unitPrice); // 設置單價
        newCartItem.setDiscount(discount); // 設置折扣
        newCartItem.setSubtotal(subtotal); // 設置小計
        newCartItem.setIsValid(0); // 新增的商品標記為有效

        // 儲存到資料庫
        cartItemsDao.save(newCartItem);
    }
	
    // 加入商品到購物車
	@Override
	public void addToCart(Integer userId, Integer productId, int quantity) {
		// 查詢用戶的購物車，如果不存在則創建新購物車
	    Cart cart = cartDao.findByUserId(userId).orElseGet(() -> createNewCart(userId));

	    // 查詢商品，確保商品存在且「已上架」
	    Products product = productService.findById(productId)
	            .orElseThrow(() -> new RuntimeException("商品不存在"));

	    // 商品狀態判斷（必須 `status = 1` 才可加入購物車）
	    if (product.getStatus() != 1) {
	        throw new RuntimeException("該商品已下架，無法加入購物車");
	    }

	    // 查詢購物車內的所有該商品的 CartItem（可能有多筆不同狀態的）
	    List<CartItems> cartItems = cartItemsDao.findByCartCartId(cart.getCartId());

	    // 找出 **有效的 CartItem（is_valid = 0）**
	    Optional<CartItems> validCartItem = cartItems.stream()
	            .filter(item -> item.getProducts().getProductId().equals(productId) && item.getIsValid() == 0)
	            .findFirst();

	    if (validCartItem.isPresent()) {
	        // **如果已有有效 `CartItem`，則合併數量**
	        CartItems existingItem = validCartItem.get();
	        existingItem.setQuantity(existingItem.getQuantity() + quantity);
	        existingItem.setSubtotal(existingItem.getUnitPrice().subtract(existingItem.getDiscount())
	                .multiply(BigDecimal.valueOf(existingItem.getQuantity())));
	        cartItemsDao.save(existingItem);
	    } else {
	        // **如果沒有有效 `CartItem`，則新增新紀錄**
	        createNewCartItem(cart, productId, quantity);
	    }
	}

	// 查詢購物車內容
	@Override
	public List<CartItemFrontendDTO> getCartItems(Integer userId) {
		// 查詢購物車
	    Cart cart = cartDao.findByUserId(userId)
	            .orElseThrow(() -> new RuntimeException("購物車不存在"));

	    // 查詢購物車內所有商品
	    List<CartItems> cartItems = cartItemsDao.findByCartCartId(cart.getCartId());

	    // 調試輸出查詢到的 CartItems
	    System.out.println("查詢到的 CartItems: " + cartItems);

	    // **檢查商品狀態並過濾無效商品**
	    List<CartItemFrontendDTO> result = cartItems.stream()
	            .peek(item -> {
	                System.out.println("處理商品 ID: " + item.getProducts().getProductId());
	                Products product = item.getProducts(); // 取得商品資訊
	                
	             // 商品下架檢查
	                if (product.getStatus() == 0) {
	                    System.out.println("商品已下架，標記為無效 (is_valid=1)");
	                    item.setIsValid(1); // **商品下架**
	                    cartItemsDao.save(item);
	                } else {
	                    // 確定比較的單價和折扣
	                    BigDecimal currentUnitPrice = product.getPrice(); // 單價應該是商品的原價
	                    BigDecimal currentDiscount = product.getSpecialPrice() != null && product.getSpecialPrice().compareTo(BigDecimal.ZERO) > 0
	                            ? product.getPrice().subtract(product.getSpecialPrice()) // 折扣 = 原價 - 特價
	                            : BigDecimal.ZERO; // 無特價時折扣為 0

	                    // 比較 `unit_price` 和 `discount`
	                    System.out.println("比較單價: 購物車[" + item.getUnitPrice() + "] 商品原價[" + currentUnitPrice + "]");
	                    System.out.println("比較折扣: 購物車[" + item.getDiscount() + "] 商品折扣[" + currentDiscount + "]");

	                    if (item.getUnitPrice().compareTo(currentUnitPrice) != 0) {
	                        System.out.println("商品單價發生變動，標記為無效 (is_valid=2)");
	                        item.setIsValid(2);
	                        cartItemsDao.save(item);
	                    } else if (item.getDiscount().compareTo(currentDiscount) != 0) {
	                        System.out.println("商品折扣發生變動，標記為無效 (is_valid=2)");
	                        item.setIsValid(2);
	                        cartItemsDao.save(item);
	                    } else {
	                        System.out.println("商品有效，保留");
	                    }
	                }
	            })
	            .filter(item -> {
	                // 調試過濾邏輯
	                System.out.println("過濾商品 ID: " + item.getProducts().getProductId() + "，isValid 值: " + item.getIsValid());
	                return item.getIsValid() == 0; // **過濾掉 is_valid != 0 的無效商品**
	            })
	            .map(this::convertToFrontendDTO) // 轉換為前台 DTO
	            .collect(Collectors.toList());

	    // 調試輸出最終返回的結果
	    System.out.println("最終返回的 CartItems: " + result);
	    return result;
	}

	// 移除購物車中的某個商品（假刪除邏輯）
	@Override
	public void removeFromCart(Integer userId, Integer productId) {
		// 查詢購物車
	    Cart cart = cartDao.findByUserId(userId)
	            .orElseThrow(() -> new RuntimeException("購物車不存在"));

	    // 查詢有效的商品記錄
	    CartItems cartItem = cartItemsDao.findByCartCartIdAndProductsProductIdAndIsValid(cart.getCartId(), productId, 0)
	            .orElseThrow(() -> new RuntimeException("有效的商品不存在於購物車中"));

	    // 假刪除：將商品標記為無效（is_valid = 3）
	    cartItem.setIsValid(3); // 標記為假刪除
	    cartItemsDao.save(cartItem); // 更新資料庫
	}

	// 修改購物車中的商品數量
    @Override
    public CartItemFrontendDTO updateCartItem(Integer userId, Integer productId, int newQuantity) {
    	// 查詢購物車
        Cart cart = cartDao.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("購物車不存在"));

        // 查詢購物車內的所有該商品的 CartItem（可能有多筆不同狀態的）
        List<CartItems> cartItems = cartItemsDao.findByCartCartId(cart.getCartId());

        // 找出 **有效的 CartItem（is_valid = 0）**
        Optional<CartItems> validCartItem = cartItems.stream()
                .filter(item -> item.getProducts().getProductId().equals(productId) && item.getIsValid() == 0)
                .findFirst();

        if (newQuantity <= 0) {
        	// **如果新的數量 <= 0，假刪除該商品**
            validCartItem.ifPresent(item -> {
                item.setIsValid(3); // 假刪除_保留原先假刪除前的數量
                cartItemsDao.save(item);
            });
        } else {
            if (validCartItem.isPresent()) {
            	// **如果已有有效 `CartItem`，則直接覆蓋數量**
                CartItems existingItem = validCartItem.get();
                existingItem.setQuantity(newQuantity); // 更新數量
                existingItem.setSubtotal(existingItem.getUnitPrice().subtract(existingItem.getDiscount())
                        .multiply(BigDecimal.valueOf(newQuantity))); // 更新小計
                cartItemsDao.save(existingItem);
            } else {
                // **如果沒有有效 `CartItem`，則新增新紀錄**
                createNewCartItem(cart, productId, newQuantity);
            }
        }
     // 轉換為 DTO 並返回
        CartItems updatedCartItem = cartItemsDao.findByCartCartId(cart.getCartId()).stream()
                .filter(item -> item.getProducts().getProductId().equals(productId) && item.getIsValid() == 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("更新後商品未找到"));
            
        // 返回 CartItemFrontendDTO
        return convertToFrontendDTO(updatedCartItem);
    }
    
    // 清空購物車_假刪除
    @Override
    public void clearCart(Integer userId) {
        Cart cart = cartDao.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("購物車不存在"));
        List<CartItems> cartItems = cartItemsDao.findByCartCartId(cart.getCartId());
        cartItems.forEach(item -> {
            item.setIsValid(3); // 標記為假刪除
            cartItemsDao.save(item);
        });
    }

}
