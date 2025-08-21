package com.cloudSerenityHotel.order.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.cloudSerenityHotel.order.dao.CartItemsDao;
import com.cloudSerenityHotel.order.dao.OrderDao;
import com.cloudSerenityHotel.order.dao.OrderItemsDao;
import com.cloudSerenityHotel.order.dto.CartItemFrontendDTO;
import com.cloudSerenityHotel.order.dto.CartTurntoOrderDTO;
import com.cloudSerenityHotel.order.dto.OrderBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderFrontendDTO;
import com.cloudSerenityHotel.order.dto.OrderItemBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderItemFrontendDTO;
import com.cloudSerenityHotel.order.model.CartItems;
import com.cloudSerenityHotel.order.model.Order;
import com.cloudSerenityHotel.order.model.OrderItems;
import com.cloudSerenityHotel.order.service.EmailService;
import com.cloudSerenityHotel.order.service.OrderService;
import com.cloudSerenityHotel.product.dao.ProductRepository;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
@Transactional // 自動交易管理員
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao; // 操作訂單主檔的 DAO
	@Autowired
	private OrderItemsDao orderItemsDao; // 操作訂單明細的 DAO
	@Autowired
	private CartItemsDao cartItemsDao; // 操作購物車明細的 DAO
	@Autowired
	private ProductRepository productDao;
	@Autowired
	private EmailService emailService;
//	@Autowired
//	private UserRepository userDao; // 查詢會員資訊用(因為Order 有關聯，用orderDao)

	// --- 後台 / 前台 DTO 轉換 ---
	@Override
	public OrderBackendDTO convertToBackendDTO(Order order) {
		if (order == null)
			return null;
		OrderBackendDTO dto = new OrderBackendDTO();
		dto.setOrderId(order.getOrderId());
		dto.setUserId(order.getUserId());
		dto.setReceiveName(order.getReceiveName());
		dto.setEmail(order.getEmail());
		dto.setPhoneNumber(order.getPhoneNumber());
		dto.setAddress(order.getAddress());
		dto.setOrderStatus(order.getOrderStatus());
		dto.setPaymentMethod(order.getPaymentMethod());
		dto.setTotalAmount(order.getTotalAmount() != null ? order.getTotalAmount().toString() : "0.00");
		dto.setFinalAmount(order.getFinalAmount() != null ? order.getFinalAmount().toString() : "0.00");
		dto.setOrderDate(
				order.getOrderDate() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getOrderDate())
						: null);
		dto.setUpdatedAt(
				order.getUpdatedAt() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getUpdatedAt())
						: null);
		// 訂單細項
		List<OrderItemBackendDTO> itemsDto = order.getOrderItems().stream().map(item -> {
			String mainImageUrl = item.getProducts().getProductImages().stream().filter(ProductImages::getIsPrimary) // 過濾出主圖
					.map(ProductImages::getImageUrl) // 取 URL
					.findFirst() // 找第一張
					.orElse(null); // 沒有就 null
			OrderItemBackendDTO itemDto = new OrderItemBackendDTO();
			itemDto.setOrderitemId(item.getOrderitemId());
			itemDto.setOrderId(item.getOrder().getOrderId());
			itemDto.setProductId(item.getProducts().getProductId());
			itemDto.setProductName(item.getProducts().getProductName());
			itemDto.setProductMainImage(mainImageUrl);
			itemDto.setProductPrice(item.getProducts().getPrice());
			itemDto.setSpecialPrice(item.getProducts().getSpecialPrice());
			itemDto.setQuantity(item.getQuantity());
			itemDto.setUnitPrice(item.getUnitPrice());
			itemDto.setDiscount(item.getDiscount());
			itemDto.setSubtotal(item.getSubtotal());
			return itemDto;
		}).collect(Collectors.toList());

		dto.setOrderItemsDtos(itemsDto);
		return dto;
	}

	@Override
	public OrderFrontendDTO convertToFrontendDTO(Order order) {
		if (order == null)
			return null;
		OrderFrontendDTO dto = new OrderFrontendDTO();
		dto.setOrderId(order.getOrderId());
		dto.setReceiveName(order.getReceiveName());
		dto.setEmail(order.getEmail());
		dto.setPhoneNumber(order.getPhoneNumber());
		dto.setAddress(order.getAddress());
		dto.setPaymentMethod(order.getPaymentMethod());
		dto.setOrderStatus(order.getOrderStatus());
		dto.setTotalAmount(order.getTotalAmount() != null ? order.getTotalAmount().toString() : "0.00");
		dto.setFinalAmount(order.getFinalAmount() != null ? order.getFinalAmount().toString() : "0.00");
		dto.setOrderDate(
				order.getOrderDate() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getOrderDate())
						: null);
		dto.setUpdatedAt(
				order.getUpdatedAt() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getUpdatedAt())
						: null);
		// 訂單細項
		List<OrderItemFrontendDTO> itemsDto = order.getOrderItems().stream().map(item -> {
			OrderItemFrontendDTO itemDto = new OrderItemFrontendDTO();
			itemDto.setOrderitemId(item.getOrderitemId());
			itemDto.setProductId(item.getProducts().getProductId());
			itemDto.setProductName(item.getProducts().getProductName());
			// 取主圖
			String mainImageUrl = item.getProducts().getProductImages().stream().filter(ProductImages::getIsPrimary)
					.map(ProductImages::getImageUrl).findFirst().orElse(null);
			itemDto.setProductMainImage(mainImageUrl);
			itemDto.setQuantity(item.getQuantity());
			itemDto.setUnitPrice(item.getUnitPrice());
			itemDto.setSpecialPrice(item.getProducts().getSpecialPrice());
			itemDto.setDiscount(item.getDiscount());
			itemDto.setSubtotal(item.getSubtotal());
			return itemDto;
		}).collect(Collectors.toList());
		dto.setOrderItemsDtos(itemsDto);
		return dto;
	}

	// --- 查詢 ---
	@Override
	public Optional<Order> findById(Integer orderId) {
	    return orderDao.findById(orderId);
	}
	
	@Override
	public List<OrderFrontendDTO> getOrdersForFrontendByUserId(Integer userId) {
		return orderDao.findByUserId(userId).stream().map(this::convertToFrontendDTO).collect(Collectors.toList());
	}

	@Override
	public OrderFrontendDTO getOrderDetailForFrontend(Integer userId, Integer orderId) {
	    Order order = orderDao.findByUserIdAndOrderId(userId, orderId);
	    return order != null ? convertToFrontendDTO(order) : null;
	}
	
	@Override
	public List<OrderFrontendDTO> getOrdersByUserIdAndStatus(Integer userId, String status) {
		return orderDao.findByUserIdAndOrderStatus(userId, status).stream().map(this::convertToFrontendDTO)
				.collect(Collectors.toList());
	}
	
	// 過渡 / legacy
	@Override
	public List<OrderBackendDTO> getOrdersByStatus(String status) {
	    return orderDao.findByOrderStatus(status)
	                   .stream()
	                   .map(this::convertToBackendDTO)
	                   .collect(Collectors.toList());
	}
	
	// 過渡 / legacy
	@Override
	public List<OrderBackendDTO> findAllOrders() {
		return orderDao.findAll().stream().map(this::convertToBackendDTO) // 呼叫剛才寫的轉換方法
				.collect(Collectors.toList());
	}

	// 過渡 / legacy
	@Override
	public OrderBackendDTO getOrderDetailsAsDTO(Integer orderId) {
		return orderDao.findById(orderId).map(this::convertToBackendDTO).orElse(null);
	}

	// 條件組合查詢
	@Override
	public List<OrderBackendDTO> findOrders(
			Integer orderId,
	        Integer userId,
	        LocalDate startDate,
	        LocalDate endDate,
	        String paymentMethod,
	        List<String> orderStatuses){ // 改成 List<String>
		Specification<Order> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (orderId != null) {
				predicates.add(cb.equal(root.get("orderId"), orderId));
			}
			if (userId != null) {
				predicates.add(cb.equal(root.get("userId"), userId));
			}
			if (startDate != null) {
			    predicates.add(cb.greaterThanOrEqualTo(root.get("orderDate"), startDate.atStartOfDay()));
			}
			// 結束日期 +1 天，然後用 <
			if (endDate != null) {
			    predicates.add(cb.lessThan(root.get("orderDate"), endDate.plusDays(1).atStartOfDay()));
			}
			if (paymentMethod != null && !paymentMethod.isEmpty()) {
				predicates.add(cb.equal(root.get("paymentMethod"), paymentMethod));
			}
			// orderStatus應該要可以多選/單選
			if (orderStatuses != null && !orderStatuses.isEmpty()) {
	            predicates.add(root.get("orderStatus").in(orderStatuses)); // 改成 in
	        }
	        return cb.and(predicates.toArray(new Predicate[0]));
	    };
	    List<Order> orders = orderDao.findAll(spec);
	    return orders.stream()
	                 .map(this::convertToBackendDTO)
	                 .collect(Collectors.toList());
	}
	
	// --- CRUD ---
	@Override
	public OrderBackendDTO insertOrderWithItems(Order order, List<OrderItems> items) {
		orderDao.save(order);
		items.forEach(item -> item.setOrder(order));
		orderItemsDao.saveAll(items);
		return convertToBackendDTO(order);
	}

	@Override
	public OrderBackendDTO updateOrder(Integer orderId, Order updatedOrder) {
		return orderDao.findById(orderId).map(existingOrder -> {
			// 檢查是否已作廢
	        if ("作廢".equals(existingOrder.getOrderStatus())) {
	            throw new RuntimeException("已作廢的訂單不可修改");
	        }
	        // 只能修改可變欄位
	        existingOrder.setReceiveName(updatedOrder.getReceiveName());
	        existingOrder.setEmail(updatedOrder.getEmail());
	        existingOrder.setPhoneNumber(updatedOrder.getPhoneNumber());
	        existingOrder.setAddress(updatedOrder.getAddress());
	        existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
	        // 不允許修改的欄位 (paymentMethod、totalAmount、finalAmount) 不動
	        // existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
	        // existingOrder.setTotalAmount(updatedOrder.getTotalAmount());
	        // existingOrder.setFinalAmount(updatedOrder.getFinalAmount());
	        Order saved = orderDao.save(existingOrder);
	        orderDao.flush(); // 強制同步到資料庫與實體
	        return convertToBackendDTO(saved);
	    }).orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + orderId));
	}
	
	// 假刪除 / 作廢訂單
	@Override
    public OrderBackendDTO voidOrder(Integer orderId) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new RuntimeException("訂單不存在"));
        order.setOrderStatus("作廢"); // 將狀態改為作廢
        orderDao.save(order);          // 儲存修改
        return convertToBackendDTO(order);           // 回傳 DTO
    }

	@Override
	public boolean deleteOrderById(Integer orderId) {
		if (orderDao.existsById(orderId)) {
			orderDao.deleteById(orderId);
			return true;
		}
		return false;
	}

	// --- 業務邏輯 ---
	
	// CartTurntoOrderDTO = 前端傳來的 "購物車轉訂單請求"
	// OrderBackendDTO = 後端回給前端的 "訂單資料"
	@Override
	public Order createOrderEntity(CartTurntoOrderDTO orderRequest) {
		Order order = new Order();
	    // 1) 基本資料
	    order.setUserId(orderRequest.getRecipient().getUserid());
	    order.setReceiveName(orderRequest.getRecipient().getReceiveName());
	    order.setEmail(orderRequest.getRecipient().getEmail());
	    order.setPhoneNumber(orderRequest.getRecipient().getPhone());
	    order.setAddress(orderRequest.getRecipient().getAddress());
	    order.setPaymentMethod(orderRequest.getRecipient().getPaymentMethod());
	    // 根據付款方式決定訂單狀態
	    if ("信用卡".equals(order.getPaymentMethod())) {
	        order.setOrderStatus("未付款"); // 生成待付款訂單
	    } else {
	        order.setOrderStatus("處理中"); // 現金/貨到付款
	    }
	    
	    // 2) 明細列表
	    List<OrderItems> orderItems = new ArrayList<>();
	    for (CartItemFrontendDTO cartItem : orderRequest.getOrderItems()) {
	        OrderItems item = new OrderItems();
	        item.setOrder(order);
	        item.setQuantity(cartItem.getQuantity());
	        Products product = productDao.findById(cartItem.getProductId())
	            .orElseThrow(() -> new RuntimeException("Product not found"));
	        item.setProducts(product);
	        item.setUnitPrice(product.getPrice());
	        item.setDiscount(product.getPrice()
	            .subtract(product.getSpecialPrice() != null ? product.getSpecialPrice() : product.getPrice()));
	        item.setSubtotal(
	            (item.getUnitPrice().subtract(item.getDiscount()))
	            .multiply(BigDecimal.valueOf(item.getQuantity()))
	        );
	        orderItems.add(item);
	    }
	    // 3) 設定明細
	    order.setOrderItems(new HashSet<>(orderItems));
	    // 4) 計算總金額
	    calculateOrderTotal(order, orderItems);
	    // 5) 儲存
	    orderDao.save(order);
	    // ✅ 寄信：訂單建立完成，不論付款是否成功
	    emailService.sendOrderCreatedEmail(order);
	    // 6) 更新購物車狀態為「結帳」
	    for (CartItemFrontendDTO cartItemDTO : orderRequest.getOrderItems()) {
	        CartItems cartItem = cartItemsDao.findById(cartItemDTO.getCartItemId())
	            .orElseThrow(() -> new RuntimeException("CartItem not found"));
	        cartItem.setIsValid(4); // 4 = 結帳
	        cartItemsDao.save(cartItem);
	    }
	    // 7) 回傳完整訂單
	    return order;
	}

		@Override
		public void calculateOrderTotal(Order order, List<OrderItems> items) {
			if (order == null || items == null)
				return; // 直接用 return; 表示「結束方法、什麼都不做」
			BigDecimal totalAmount = BigDecimal.ZERO;
			BigDecimal finalAmount = BigDecimal.ZERO;
			for (OrderItems item : items) {
				Products product = item.getProducts();
				BigDecimal price = product.getPrice();
				BigDecimal specialPrice = product.getSpecialPrice();
				// 原價
				item.setUnitPrice(price);
				// 折扣計算
				if (specialPrice != null && specialPrice.compareTo(BigDecimal.ZERO) > 0) {
					item.setDiscount(price.subtract(specialPrice)); // subtract() 就是做減法，也就是「左邊減右邊」
				} else {
					item.setDiscount(BigDecimal.ZERO);
					specialPrice = price; // 沒有特價就用原價
				}
				// 小計計算
				BigDecimal subtotal = price.subtract(item.getDiscount())
																		.multiply(BigDecimal.valueOf(item.getQuantity()));
				item.setSubtotal(subtotal);
				// 累加總金額（原價 * 數量）
				totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
				// 累加最終金額（特價 * 數量）
				finalAmount = finalAmount.add(specialPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
			}
			// 設定訂單總額
			order.setTotalAmount(totalAmount);
			order.setFinalAmount(finalAmount);
		}

		@Override
		public void paymentSuccess(Integer orderId) {
			Order dbOrder = orderDao.findById(orderId)
		            					.orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + orderId));
		    // 防止重複更新 & 寄信
		    if ("Paid".equals(dbOrder.getOrderStatus())) {
		        System.out.println("訂單已標記為已付款，略過更新與寄信");
		        return;}
		    // 更新訂單狀態
		    dbOrder.setOrderStatus("已付款"); // 訂單狀態: Paid = 已付款
		    orderDao.save(dbOrder);
		    // 呼叫 EmailService直接給完整的訂單資料， 發送付款成功通知
		    emailService.sendPaymentSuccessEmail(dbOrder);
		}

}