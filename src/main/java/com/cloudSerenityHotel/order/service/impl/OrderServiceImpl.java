package com.cloudSerenityHotel.order.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.order.dao.OrderDao;
import com.cloudSerenityHotel.order.dao.OrderItemsDao;
import com.cloudSerenityHotel.order.dto.OrderDTO;
import com.cloudSerenityHotel.order.dto.OrderItemDTO;
import com.cloudSerenityHotel.order.model.OrderBean;
import com.cloudSerenityHotel.order.model.OrderItemsBean;
import com.cloudSerenityHotel.order.service.OrderService;

import jakarta.transaction.Transactional;

// JPA 的 save 方法會根據物件是否有主鍵來自動選擇是執行「新增」還是「更新」。
@Service
@Transactional // 自動交易管理員
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderItemsDao orderItemsDao;

	// DTO 的轉換功能
	@Override
	public OrderDTO convertToDTO(OrderBean order) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(order.getOrderId());
		orderDTO.setUserId(order.getUserId());
		orderDTO.setReceiveName(order.getReceiveName());
		orderDTO.setEmail(order.getEmail());
		orderDTO.setPhoneNumber(order.getPhoneNumber());
		orderDTO.setAddress(order.getAddress());
		orderDTO.setOrderStatus(order.getOrderStatus());
		orderDTO.setPaymentMethod(order.getPaymentMethod());
		orderDTO.setTotalAmount(order.getTotalAmount().toPlainString());
		orderDTO.setDiscountAmount(order.getDiscountAmount().toPlainString());
		orderDTO.setFinalAmount(order.getFinalAmount().toPlainString());
		orderDTO.setOrderDate(formatter.format(order.getOrderDate()));
		orderDTO.setUpdatedAt(formatter.format(order.getUpdatedAt()));

		List<OrderItemDTO> orderItemDTOs = order.getOrderItemsBeans().stream().map(item -> {
			OrderItemDTO orderItemDTO = new OrderItemDTO();
			orderItemDTO.setOrderitemId(item.getOrderitemId());
			orderItemDTO.setOrderId(order.getOrderId()); // 確保 orderId 被設置
			orderItemDTO.setProductId(item.getProducts().getProductId());
			orderItemDTO.setProductName(item.getProducts().getProductName());//這裡Bean有修改名稱
			orderItemDTO.setProductPrice(item.getProducts().getPrice());
			orderItemDTO.setQuantity(item.getQuantity());
			orderItemDTO.setUnitPrice(item.getUnitPrice());
			orderItemDTO.setDiscount(item.getDiscount());
			orderItemDTO.setSubtotal(item.getSubtotal());
			return orderItemDTO;
		}).collect(Collectors.toList());

		orderDTO.setOrderItemsDtos(orderItemDTOs);

		return orderDTO;
	}

	// 查詢所有訂單
	@Override
	public List<OrderDTO> findAllOrders() {
		List<OrderBean> orders = orderDao.findAll(Sort.by(Sort.Direction.ASC, "orderId"));
		return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	// 分頁
	@Override
	public Page<OrderDTO> findOrdersWithPagination(int page, int size) {
		// 分頁參數：page (從 0 開始)，size (每頁筆數)
	    Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
	    Page<OrderBean> orderPage = orderDao.findAll(pageable);

	    // 將分頁結果轉換為 DTO
		return orderPage.map(this::convertToDTO);
	}


	// 查詢單筆訂單
	@Override
	public OrderDTO getOrderDetailsAsDTO(Integer orderId) {
	    // 查詢訂單，若不存在則拋出 NoSuchElementException
	    OrderBean order = orderDao.findById(orderId)
	            .orElseThrow(() -> new NoSuchElementException("訂單不存在，ID: " + orderId));
	    // 將訂單實體轉換為 DTO
	    return convertToDTO(order);
	}

	// 刪除訂單
	@Override
	public boolean deleteOrderById(Integer orderId) {
		try {
			OrderBean order = orderDao.findById(orderId)
					.orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + orderId));
			orderDao.delete(order); // 依賴 CascadeType.ALL，自動刪除細項
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 為已存在的訂單新增訂單細項_未使用到
	@Override
	public List<OrderItemsBean> insertItemsToExistingOrder(Integer orderId, List<OrderItemsBean> items) {
		OrderBean order = orderDao.findById(orderId).orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + orderId));
		for (OrderItemsBean item : items) {
			item.setOrder(order);
		}
		return orderItemsDao.saveAll(items);
	}

	// 更新單一訂單細項_未使用到
	@Override
	public OrderItemsBean updateOrderItem(OrderItemsBean updatedItem) {
		// 檢查訂單細項是否存在
		OrderItemsBean existingItem = orderItemsDao.findById(updatedItem.getOrderitemId())
				.orElseThrow(() -> new RuntimeException("訂單細項不存在，ID: " + updatedItem.getOrderitemId()));

		// 更新允許的字段
		existingItem.setQuantity(updatedItem.getQuantity());
		existingItem.setUnitPrice(updatedItem.getUnitPrice());
		existingItem.setDiscount(updatedItem.getDiscount());

		// 保存更新後的細項
		existingItem = orderItemsDao.save(existingItem);

		// 更新訂單總金額
		OrderBean order = existingItem.getOrder();
		List<OrderItemsBean> items = new ArrayList<>(order.getOrderItemsBeans());
		calculateOrderTotal(order, items);
		orderDao.save(order); // 保存更新後的訂單主檔

		return existingItem;
	}

	// 插入訂單與訂單細項
	@Override
	public OrderDTO insertOrderWithItems(OrderBean orderBean, List<OrderItemsBean> items) {
		// 設置細項的 subtotal 並與訂單關聯
		for (OrderItemsBean item : items) {
			// 確保 product_id 已設置
			if (item.getProducts() == null || item.getProducts().getProductId() == null) {
				throw new RuntimeException("產品資訊未正確設置");
			}

			// 確保 subtotal 被計算
			BigDecimal subtotal = item.getUnitPrice()
					.subtract(item.getDiscount() != null ? item.getDiscount() : BigDecimal.ZERO)
					.multiply(BigDecimal.valueOf(item.getQuantity()));
			item.setSubtotal(subtotal); // 設置小計

			// 關聯訂單
			item.setOrder(orderBean);
		}

		// 計算訂單總金額
		calculateOrderTotal(orderBean, items);

		// 保存訂單及細項
		orderDao.save(orderBean);
		orderItemsDao.saveAll(items);

		return convertToDTO(orderBean);
	}

	@Override
	public OrderDTO updateOrder(Integer orderId, OrderBean updatedOrder) {
	    // 查詢舊訂單
	    OrderBean existingOrder = orderDao.findById(orderId)
	            .orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + orderId));

	    // 僅更新允許變動的欄位
	    existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
	    existingOrder.setReceiveName(updatedOrder.getReceiveName());
	    existingOrder.setEmail(updatedOrder.getEmail());
	    existingOrder.setPhoneNumber(updatedOrder.getPhoneNumber());
	    existingOrder.setAddress(updatedOrder.getAddress());
	    existingOrder.setUpdatedAt(new Timestamp(System.currentTimeMillis())); // 更新時間戳

	    // 保存更新後的訂單
	    orderDao.save(existingOrder);

	    // 將更新後的訂單轉換為 DTO 並返回
	    return convertToDTO(existingOrder);
	}

	// 計算訂單總金額
	@Override
	public void calculateOrderTotal(OrderBean order, List<OrderItemsBean> items) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal discountAmount = BigDecimal.ZERO;

		for (OrderItemsBean item : items) {
			BigDecimal itemSubtotal = item.getUnitPrice().subtract(item.getDiscount())
					.multiply(BigDecimal.valueOf(item.getQuantity()));
			totalAmount = totalAmount.add(itemSubtotal);
			discountAmount = discountAmount.add(item.getDiscount().multiply(BigDecimal.valueOf(item.getQuantity())));
		}

		order.setTotalAmount(totalAmount); // 設置總金額
		order.setDiscountAmount(discountAmount); // 設置折扣金額
		order.setFinalAmount(totalAmount.subtract(discountAmount)); // 設置最終金額
	}
}
