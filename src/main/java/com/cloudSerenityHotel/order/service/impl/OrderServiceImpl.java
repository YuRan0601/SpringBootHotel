package com.cloudSerenityHotel.order.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloudSerenityHotel.order.dao.CartItemsDao;
import com.cloudSerenityHotel.order.dao.OrderDao;
import com.cloudSerenityHotel.order.dao.OrderItemsDao;
import com.cloudSerenityHotel.order.dto.CartTurntoOrderDTO;
import com.cloudSerenityHotel.order.dto.OrderBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderFrontendDTO;
import com.cloudSerenityHotel.order.dto.OrderItemBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderItemFrontendDTO;
import com.cloudSerenityHotel.order.model.Order;
import com.cloudSerenityHotel.order.model.OrderItems;
import com.cloudSerenityHotel.order.service.OrderService;
import com.cloudSerenityHotel.product.dao.ProductRepository;
import com.cloudSerenityHotel.product.model.ProductImages;
import com.cloudSerenityHotel.product.model.Products;

import jakarta.transaction.Transactional;

@Service
@Transactional // 自動交易管理員
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemsDao orderItemsDao;
	@Autowired
	private ProductRepository productDao;
	@Autowired
	private CartItemsDao cartItemsDao;

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
	public List<OrderBackendDTO> findAllOrders() {
		return orderDao.findAll().stream().map(this::convertToBackendDTO) // 呼叫剛才寫的轉換方法
				.collect(Collectors.toList());
	}

	@Override
	public OrderBackendDTO getOrderDetailsAsDTO(Integer orderId) {
		return orderDao.findById(orderId).map(this::convertToBackendDTO).orElse(null);
	}

	@Override
	public List<OrderFrontendDTO> getOrdersForFrontendByUserId(Integer userId) {
		return orderDao.findByUserId(userId).stream().map(this::convertToFrontendDTO).collect(Collectors.toList());
	}

	@Override
	public List<OrderFrontendDTO> getOrdersByUserIdAndStatus(Integer userId, String status) {
		return orderDao.findByUserIdAndOrderStatus(userId, status).stream().map(this::convertToFrontendDTO)
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
			existingOrder.setReceiveName(updatedOrder.getReceiveName());
			existingOrder.setEmail(updatedOrder.getEmail());
			existingOrder.setPhoneNumber(updatedOrder.getPhoneNumber());
			existingOrder.setAddress(updatedOrder.getAddress());
			existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
			existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
			existingOrder.setTotalAmount(updatedOrder.getTotalAmount());
			existingOrder.setFinalAmount(updatedOrder.getFinalAmount());
			// 建立時間不動，更新時間交由 @PreUpdate
			Order saved = orderDao.save(existingOrder);
			return convertToBackendDTO(saved);
		}).orElse(null);
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
	@Override
	public OrderBackendDTO createOrder(CartTurntoOrderDTO orderRequest) {
		Order order = new Order();
		// 設定基本資料，範例：
		order.setUserId(orderRequest.getUserId());
		order.setReceiveName(orderRequest.getReceiveName());
		order.setEmail(orderRequest.getEmail());
		order.setPhoneNumber(orderRequest.getPhoneNumber());
		order.setAddress(orderRequest.getAddress());
		order.setOrderStatus("Pending");
		order.setPaymentMethod(orderRequest.getPaymentMethod());

		List<OrderItems> orderItems = new ArrayList<>();
		for (CartItemDTO cartItem : orderRequest.getCartItems()) {
			OrderItems item = new OrderItems();
			item.setOrder(order);
			item.setQuantity(cartItem.getQuantity());
			// 從商品資料庫載入商品並設定
			Products product = productDao.findById(cartItem.getProductId())
					.orElseThrow(() -> new RuntimeException("Product not found"));
			item.setProducts(product);
			item.setUnitPrice(product.getPrice());
			item.setDiscount(product.getPrice()
					.subtract(product.getSpecialPrice() != null ? product.getSpecialPrice() : product.getPrice()));
			item.setSubtotal((item.getUnitPrice().subtract(item.getDiscount()))
					.multiply(BigDecimal.valueOf(item.getQuantity())));
			orderItems.add(item);
		}
		order.setOrderItems(new HashSet<>(orderItems));
		calculateOrderTotal(order, orderItems);
		orderDao.save(order);
		return convertToBackendDTO(order);
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
			orderDao.findById(orderId).ifPresent(order -> {
				order.setOrderStatus("Paid");
				orderDao.save(order);
				// 這裡可以放後續的業務邏輯，如發送郵件通知
			});
		}
}