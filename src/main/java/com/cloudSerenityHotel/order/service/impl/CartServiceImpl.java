package com.cloudSerenityHotel.order.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudSerenityHotel.order.dao.CartDao;
import com.cloudSerenityHotel.order.dao.CartItemsDao;
import com.cloudSerenityHotel.order.dto.CartItemFrontendDTO;
import com.cloudSerenityHotel.order.dto.MemberForCartFrontendDTO;
import com.cloudSerenityHotel.order.model.Cart;
import com.cloudSerenityHotel.order.model.CartItems;
import com.cloudSerenityHotel.order.service.CartService;
import com.cloudSerenityHotel.product.dao.ProductRepository;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import com.cloudSerenityHotel.user.model.Member;
import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.model.UserRepository;

@Service
@Transactional // 自動交易管理員
public class CartServiceImpl implements CartService {

	@Autowired
	private CartDao cartDao; // 操作購物車主檔的 DAO
	@Autowired
	private CartItemsDao cartItemsDao; // 操作購物車明細的 DAO
	@Autowired
	private ProductRepository productDao; // 查詢商品資訊用
	@Autowired
	private UserRepository userDao; // 查詢會員資訊用

	// --- DTO 轉換 ---
	@Override
	public CartItemFrontendDTO convertToFrontendDTO(CartItems cartItem) {
		// 將購物車明細 Entity 轉成前端用 DTO
		CartItemFrontendDTO dto = new CartItemFrontendDTO();
		// 取出原價/特價（特價可能為 null）
		BigDecimal originalPrice = cartItem.getProducts().getPrice(); // 原價
		BigDecimal specialPrice = cartItem.getProducts().getSpecialPrice(); // 特價（可能是 null）

		dto.setCartItemId(cartItem.getCartItemId());
		dto.setProductId(cartItem.getProducts().getProductId());
		dto.setProductName(cartItem.getProducts().getProductName());
		dto.setQuantity(cartItem.getQuantity());
		// 單價（unitPrice）固定為「原價」
		dto.setUnitPrice(originalPrice);
		// 折扣金額 = 原價 - (有特價就用特價，否則用原價 → 變成 0)
		BigDecimal discount = originalPrice.subtract((specialPrice != null) ? specialPrice : originalPrice);
		dto.setDiscount(discount);
		// 小計 = (特價或原價) × 數量
		dto.setSubtotal(originalPrice.subtract(discount).multiply(BigDecimal.valueOf(cartItem.getQuantity())));
		// 商品主要圖片
		dto.setImageUrl(cartItem.getProducts().getProductImages().stream()
		        .filter(ProductImages::getIsPrimary)   // 篩選出 isPrimary = true
		        .findFirst()                           // 找到第一張主圖
		        .map(ProductImages::getImageUrl)       // 取出 URL
		        .orElse(null)                          // 如果沒有主圖就回傳 null
				);
		return dto;
	}

	// --- 取得購物車頁面需要的會員資料 ---
	@Override
	public MemberForCartFrontendDTO getMemberForCart(int userId) {
		// 1. 從資料庫查詢會員（若不存在則丟例外）
		User user = userDao.findById(userId)
								.orElseThrow(() -> new RuntimeException("會員不存在，ID: " + userId));
		Member member = user.getMember();
		// 2. 建立 DTO 並填入資料
		MemberForCartFrontendDTO dto = new MemberForCartFrontendDTO();
		dto.setUserid(user.getUserId());
		dto.setUserName(user.getUserName()); // 會員姓名
		dto.setEmail(user.getEmail());
		dto.setPhone(member.getPhone());
		dto.setAddress(member.getAddress());
		// 3.不從歷史訂單抓付款方式
		dto.setPaymentMethod(null); // 前端選擇現金/信用卡
		// 4. 回傳 DTO
		return dto;
	}

	// --- 新增商品到購物車 ---
	@Override
	public Cart createNewCart(Integer userId) {
		User user = userDao.findById(userId)
								.orElseThrow(() -> new RuntimeException("會員不存在"));
		Cart cart = new Cart();
		cart.setMember(user.getMember());
		return cartDao.save(cart);
	}

	@Override
	public CartItems createNewCartItem(Cart cart, Integer productId, int quantity) {
		// 1. 從資料庫查詢商品，若不存在則丟例外
		Products product = productDao.findById(productId)
											.orElseThrow(() -> new RuntimeException("商品不存在"));
		BigDecimal specialPrice = product.getSpecialPrice(); // 特價（可能是 null）
		BigDecimal unitPrice = product.getPrice();
		BigDecimal discount = unitPrice.subtract((specialPrice != null) ? specialPrice : unitPrice);
		BigDecimal subtotal = unitPrice.subtract(discount).multiply(BigDecimal.valueOf(quantity));
		 // 2. 建立新的購物車明細物件
		CartItems newItem = new CartItems();
		newItem.setCart(cart);
		newItem.setProducts(product);
		newItem.setQuantity(quantity);
		newItem.setUnitPrice(unitPrice);
		newItem.setDiscount(discount);
		newItem.setSubtotal(subtotal);
		newItem.setIsValid(0);
		
		// ✅ 更新購物車的時間戳
	    cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
	    cartDao.save(cart);
		
	    // 3. 儲存到資料庫並回傳剛新增的購物車明細
	    return cartItemsDao.save(newItem);
	}
	
	/*
	 * 成功 → 回傳 DTO
	 * 失敗 → 丟 RuntimeException
	 */
	@Override
	public CartItemFrontendDTO addToCart(Integer userId, Integer productId, int quantity) {
		 // 1. 查詢用戶的購物車，如果不存在則創建新購物車
	    Cart cart = cartDao.findByMember_UserId(userId)
	            .orElseGet(() -> createNewCart(userId));
	    // 2. 查詢商品，確保商品存在且已上架
	    Products product = productDao.findById(productId)
	            .orElseThrow(() -> new RuntimeException("商品不存在"));
	    if (product.getStatus() != 1) {
	        throw new RuntimeException("商品未上架，無法加入購物車");
	    }
	    // 3. 查詢購物車內的有效 CartItem()
	    Optional<CartItems> existingItemOpt = cartItemsDao
	            .findByCartCartIdAndProductsProductIdAndIsValid(cart.getCartId(), productId, 0);
	    CartItems savedItem;
	    if (existingItemOpt.isPresent()) {
	        // 4a. 已存在 → 更新數量與小計
	        CartItems existingItem = existingItemOpt.get();
	        existingItem.setQuantity(existingItem.getQuantity() + quantity);
	        existingItem.setSubtotal(existingItem.getUnitPrice()
	                .subtract(existingItem.getDiscount())
	                .multiply(BigDecimal.valueOf(existingItem.getQuantity())));
	        savedItem = cartItemsDao.save(existingItem);
	    } else {
	        // 4b. 不存在 → 新增購物車明細
	        savedItem = createNewCartItem(cart, productId, quantity);
	    }
	    // 5. 回傳前端 DTO
	    return convertToFrontendDTO(savedItem);
	}

	// --- 取得購物車的所有商品 ---
	@Override
	public List<CartItemFrontendDTO> getCartItems(Integer userId) {
		// 1. 找到該會員的購物車
		Cart cart = cartDao.findByMember_UserId(userId).orElseThrow(() -> new RuntimeException("購物車不存在"));
		// 2. 取得購物車內所有商品
		List<CartItems> cartItems = cartItemsDao.findByCartCartId(cart.getCartId());
		// 3. 過濾無效商品，並檢查商品狀態/價格是否變動
		List<CartItemFrontendDTO> result = cartItems.stream().peek(item -> {
			Products product = item.getProducts();
			// 商品下架檢查
			if (product.getStatus() == 0) {
				System.out.println("商品已下架，標記為無效 (is_valid=1)");
				item.setIsValid(1); // **商品下架**
				cartItemsDao.save(item);
			} else {
				// 確定比較的單價和折扣
				BigDecimal currentUnitPrice = product.getPrice(); // 單價應該是商品的原價
				BigDecimal currentDiscount = product.getSpecialPrice() != null
						&& product.getSpecialPrice().compareTo(BigDecimal.ZERO) > 0
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
		}).filter(item -> item.getIsValid() == 0) // 過濾掉無效商品
				.map(this::convertToFrontendDTO) // 轉 DTO
				.collect(Collectors.toList());
		return result;
	}

	// --- 移除購物車中的單一商品_假刪除IsValid(3) ---
	@Override
	public void removeFromCart(Integer userId, Integer productId) {
		// 1. 查詢購物車
	    Cart cart = cartDao.findByMember_UserId(userId)
	            			.orElseThrow(() -> new RuntimeException("購物車不存在"));
	    // 2. 查詢有效的商品記錄
	    CartItems cartItem = cartItemsDao.findByCartCartIdAndProductsProductIdAndIsValid(cart.getCartId(), productId, 0)
            									.orElseThrow(() -> new RuntimeException("有效的商品不存在於購物車中"));
	    cartItem.setIsValid(3); // 3. 假刪除：將商品標記為無效（is_valid = 3）
	    cartItemsDao.save(cartItem); // 4. 更新資料庫
	    
	    // ✅ 更新購物車的時間戳
	    cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
	    cartDao.save(cart);
	}

	// --- 清空購物車_假刪除IsValid(3)---
	@Override
	public void clearCart(Integer userId) {
		Cart cart = cartDao.findByMember_UserId(userId)
                			.orElseThrow(() -> new RuntimeException("購物車不存在"));
        List<CartItems> cartItems = cartItemsDao.findByCartCartId(cart.getCartId());
        cartItems.forEach(item -> {
            item.setIsValid(3); // 假刪除：將商品標記為無效（is_valid = 3）
            cartItemsDao.save(item);
            
            // ✅ 更新購物車的時間戳
    	    cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    	    cartDao.save(cart);
        });
    }

	// --- 更新購物車中商品的數量 ---
	@Override
	public CartItemFrontendDTO updateCartItem(Integer userId, Integer productId, int newQuantity) {
		 // 1. 找購物車
	    Cart cart = cartDao.findByMember_UserId(userId)
	            			.orElseThrow(() -> new RuntimeException("購物車不存在"));
	    // 2. 找該商品且有效
	    CartItems cartItem = cartItemsDao
	            .findByCartCartIdAndProductsProductIdAndIsValid(cart.getCartId(), productId, 0)
	            .orElseThrow(() -> new RuntimeException("商品不存在或已無效"));
	    // 3. 更新數量與小計
	    cartItem.setQuantity(newQuantity);
	    cartItem.setSubtotal(cartItem.getUnitPrice().subtract(cartItem.getDiscount())
	            .multiply(BigDecimal.valueOf(newQuantity)));
	    cartItemsDao.save(cartItem);
	    // 4. 回傳前端 DTO
	    return convertToFrontendDTO(cartItem);
	}

}