package com.cloudSerenityHotel.order.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.order.dao.CartItemsDao;
import com.cloudSerenityHotel.order.dao.OrderDao;
import com.cloudSerenityHotel.order.dao.OrderItemsDao;
import com.cloudSerenityHotel.order.dto.CartItemFrontendDTO;
import com.cloudSerenityHotel.order.dto.CartTurntoOrderDTO;
import com.cloudSerenityHotel.order.dto.MemberForCartFrontendDTO;
import com.cloudSerenityHotel.order.dto.OrderBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderFrontendDTO;
import com.cloudSerenityHotel.order.dto.OrderItemBackendDTO;
import com.cloudSerenityHotel.order.dto.OrderItemFrontendDTO;
import com.cloudSerenityHotel.order.model.CartItems;
import com.cloudSerenityHotel.order.model.Order;
import com.cloudSerenityHotel.order.model.OrderItems;
import com.cloudSerenityHotel.order.service.OrderService;
import com.cloudSerenityHotel.product.dao.ProductRepository;
import com.cloudSerenityHotel.product.model.Products;

import jakarta.transaction.Transactional;

// JPA 的 save 方法會根據物件是否有主鍵來自動選擇是執行「新增」還是「更新」。
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

	// 後台DTO 的轉換功能
	@Override
	public OrderBackendDTO convertToBackendDTO(Order order) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		OrderBackendDTO orderDTO = new OrderBackendDTO();
		orderDTO.setOrderId(order.getOrderId());
		orderDTO.setUserId(order.getUserId());
		orderDTO.setReceiveName(order.getReceiveName());
		orderDTO.setEmail(order.getEmail());
		orderDTO.setPhoneNumber(order.getPhoneNumber());
		orderDTO.setAddress(order.getAddress());
		orderDTO.setOrderStatus(order.getOrderStatus());
		orderDTO.setPaymentMethod(order.getPaymentMethod());
		orderDTO.setTotalAmount(order.getTotalAmount().toPlainString());
		
		// 處理 discountAmount 可能為 null 的情況
	    BigDecimal discountAmount = order.getDiscountAmount();
	    if (discountAmount == null) {
	        discountAmount = BigDecimal.ZERO;  // 若為 null，設為 0
	    }
	    orderDTO.setDiscountAmount(discountAmount.toPlainString());

	    // 處理 finalAmount 可能為 null 的情況
	    BigDecimal finalAmount = order.getFinalAmount();
	    if (finalAmount == null) {
	        finalAmount = BigDecimal.ZERO;  // 若為 null，設為 0
	    }
	    orderDTO.setFinalAmount(finalAmount.toPlainString());
	    
		orderDTO.setOrderDate(formatter.format(order.getOrderDate()));
		orderDTO.setUpdatedAt(formatter.format(order.getUpdatedAt()));

		List<OrderItemBackendDTO> orderItemDTOs = order.getOrderItemsBeans().stream().map(item -> {
			OrderItemBackendDTO orderItemDTO = new OrderItemBackendDTO();
			orderItemDTO.setOrderitemId(item.getOrderitemId());
			orderItemDTO.setOrderId(order.getOrderId()); // 確保 orderId 被設置
			orderItemDTO.setProductId(item.getProducts().getProductId());
			orderItemDTO.setProductName(item.getProducts().getProductName());// 這裡Bean有修改名稱
			orderItemDTO.setProductPrice(item.getProducts().getPrice()); // 商品原價
			orderItemDTO.setSpecialPrice(item.getProducts().getSpecialPrice());  // 特價
			orderItemDTO.setQuantity(item.getQuantity());
			orderItemDTO.setUnitPrice(item.getUnitPrice());
			orderItemDTO.setDiscount(item.getDiscount()); // 折扣
			orderItemDTO.setSubtotal(item.getSubtotal());
			return orderItemDTO;
		}).collect(Collectors.toList());

		orderDTO.setOrderItemsDtos(orderItemDTOs);

		return orderDTO;
	}

	// 查詢所有訂單
	@Override
	public List<OrderBackendDTO> findAllOrders() {
		List<Order> orders = orderDao.findAll(Sort.by(Sort.Direction.ASC, "orderId"));
		return orders.stream().map(this::convertToBackendDTO).collect(Collectors.toList());
	}

	// 分頁_未使用
	@Override
	public Page<OrderBackendDTO> findOrdersWithPagination(int page, int size) {
		// 分頁參數：page (從 0 開始)，size (每頁筆數)
		Pageable pageable = PageRequest.of(page, size, Sort.by("orderId").ascending());
		Page<Order> orderPage = orderDao.findAll(pageable);

		// 將分頁結果轉換為 DTO
		return orderPage.map(this::convertToBackendDTO);
	}

	// 查詢單筆訂單
	@Override
	public OrderBackendDTO getOrderDetailsAsDTO(Integer orderId) {
		// 查詢訂單，若不存在則拋出 NoSuchElementException
		Order order = orderDao.findById(orderId)
				.orElseThrow(() -> new NoSuchElementException("訂單不存在，ID: " + orderId));
		// 將訂單實體轉換為 DTO
		return convertToBackendDTO(order);
	}

	// 刪除訂單
	@Override
	public boolean deleteOrderById(Integer orderId) {
		try {
			Order order = orderDao.findById(orderId)
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
	public List<OrderItems> insertItemsToExistingOrder(Integer orderId, List<OrderItems> items) {
		Order order = orderDao.findById(orderId).orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + orderId));
		for (OrderItems item : items) {
			item.setOrder(order);
		}
		return orderItemsDao.saveAll(items);
	}

	// 更新單一訂單細項_未使用到
	@Override
	public OrderItems updateOrderItem(OrderItems updatedItem) {
		// 檢查訂單細項是否存在
		OrderItems existingItem = orderItemsDao.findById(updatedItem.getOrderitemId())
				.orElseThrow(() -> new RuntimeException("訂單細項不存在，ID: " + updatedItem.getOrderitemId()));

		// 更新允許的字段
		existingItem.setQuantity(updatedItem.getQuantity());
		existingItem.setUnitPrice(updatedItem.getUnitPrice());
		existingItem.setDiscount(updatedItem.getDiscount());

		// 保存更新後的細項
		existingItem = orderItemsDao.save(existingItem);

		// 更新訂單總金額
		Order order = existingItem.getOrder();
		List<OrderItems> items = new ArrayList<>(order.getOrderItemsBeans());
		calculateOrderTotal(order, items);
		orderDao.save(order); // 保存更新後的訂單主檔

		return existingItem;
	}

	// 插入訂單與訂單細項
	@Override
	public OrderBackendDTO insertOrderWithItems(Order orderBean, List<OrderItems> items) {
		// 設置細項的 subtotal 並與訂單關聯
		for (OrderItems item : items) {
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

		return convertToBackendDTO(orderBean);
	}

	@Override
	public OrderBackendDTO updateOrder(Integer orderId, Order updatedOrder) {
		// 查詢舊訂單
		Order existingOrder = orderDao.findById(orderId)
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
		return convertToBackendDTO(existingOrder);
	}

	// 計算訂單總金額
	/*
	 * 在 calculateOrderTotal 方法中加入對商品表格的查詢，抓取特價價格（specialPrice）。如果沒有特價，則使用原價計算。
	 */
	@Override
	public void calculateOrderTotal(Order order, List<OrderItems> items) {
		 BigDecimal totalAmount = BigDecimal.ZERO;  // 訂單總金額（原價 * 數量）
		    BigDecimal discountAmount = BigDecimal.ZERO; // 訂單總折扣金額

		    // 計算商品的總金額和折扣
		    for (OrderItems item : items) {
		        // 查詢商品的特價（如果有）
		        BigDecimal specialPrice = item.getProducts().getSpecialPrice();
		        BigDecimal unitPrice = item.getProducts().getPrice(); // 原價

		        // 計算商品的小計：原價 - (原價 - 特價) * 數量
		        BigDecimal itemSubtotal;
		        if (specialPrice != null) {
		            // 商品的小計：原價 - (原價 - 特價) * 數量
		            itemSubtotal = unitPrice.subtract(unitPrice.subtract(specialPrice)).multiply(BigDecimal.valueOf(item.getQuantity()));
		            // 累加折扣金額（每個商品的折扣金額）
		            BigDecimal itemDiscount = unitPrice.subtract(specialPrice).multiply(BigDecimal.valueOf(item.getQuantity()));
		            discountAmount = discountAmount.add(itemDiscount);
		        } else {
		            // 如果沒有特價，則小計為原價 * 數量
		            itemSubtotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
		        }

		        // 累加商品的小計到訂單總金額（訂單總金額是原價 * 數量的加總）
		        totalAmount = totalAmount.add(unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
		        
		        // 設置該商品的小計
		        item.setSubtotal(itemSubtotal);
		    }

		    // 計算點數折扣（每 10 點對應 1 元）
		    if (order.getPointsDiscount() > 0) {
		        BigDecimal pointsDiscount = BigDecimal.valueOf(order.getPointsDiscount()).divide(BigDecimal.valueOf(10), 2, RoundingMode.DOWN);
		        discountAmount = discountAmount.add(pointsDiscount); // 累加點數折扣
		    }

		    // 設置訂單的金額資訊
		    order.setTotalAmount(totalAmount); // 訂單總金額 = 原價 * 數量
		    order.setDiscountAmount(discountAmount); // 訂單總折扣金額 = 商品折扣 + 點數折扣
		    order.setFinalAmount(totalAmount.subtract(discountAmount)); // 最終金額 = 總金額 - 總折扣金額
		}
	
	
		private void calculateOrderTotal1(Order order, List<OrderItems> items) {
			 BigDecimal totalAmount = BigDecimal.ZERO;  // 訂單總金額（原價 * 數量）
			    BigDecimal discountAmount = BigDecimal.ZERO; // 訂單總折扣金額
	
			    // 計算商品的總金額和折扣
	//		    for (OrderItems item : items) {
	//		        // 查詢商品的特價（如果有）
	//		        BigDecimal specialPrice = item.getProducts().getSpecialPrice();
	//		        BigDecimal unitPrice = item.getProducts().getPrice(); // 原價
	//
	//		        // 計算商品的小計：原價 - (原價 - 特價) * 數量
	//		        BigDecimal itemSubtotal;
	//		        if (specialPrice != null) {
	//		            // 商品的小計：原價 - (原價 - 特價) * 數量
	//		            itemSubtotal = unitPrice.subtract(unitPrice.subtract(specialPrice).multiply(BigDecimal.valueOf(item.getQuantity())));
	//		            // 累加折扣金額（每個商品的折扣金額）
	//		            BigDecimal itemDiscount = unitPrice.subtract(specialPrice).multiply(BigDecimal.valueOf(item.getQuantity()));
	//		            discountAmount = discountAmount.add(itemDiscount);
	//		        } else {
	//		            // 如果沒有特價，則小計為原價 * 數量
	//		            itemSubtotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
	//		        }
	//
	//		        // 累加商品的小計到訂單總金額（訂單總金額是原價 * 數量的加總）
	//		        totalAmount = totalAmount.add(unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
	//		        
	//		        // 設置該商品的小計
	//		        item.setSubtotal(itemSubtotal);
	//		    }
	
			    // 計算點數折扣（每 10 點對應 1 元）
			    if (order.getPointsDiscount() > 0) {
			        BigDecimal pointsDiscount = BigDecimal.valueOf(order.getPointsDiscount()).divide(BigDecimal.valueOf(10), 2, RoundingMode.DOWN);
			        discountAmount = discountAmount.add(pointsDiscount); // 累加點數折扣
			    }
	
			    // 設置訂單的金額資訊
			    order.setTotalAmount(totalAmount); // 訂單總金額 = 原價 * 數量
			    order.setDiscountAmount(discountAmount); // 訂單總折扣金額 = 商品折扣 + 點數折扣
			    order.setFinalAmount(totalAmount.subtract(discountAmount)); // 最終金額 = 總金額 - 總折扣金額
			}

	//========================================================================================================================	
	
	// 前台使用者查詢
	// 將 OrderBean 轉換為 OrderFrontendDTO
	public OrderFrontendDTO convertToFrontendDTO(Order order) {
	    // 格式化日期
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	    // 轉換訂單細項
	    List<OrderItemFrontendDTO> orderItemDTOs = order.getOrderItemsBeans().stream().map(item -> {
	        OrderItemFrontendDTO orderItemDTO = new OrderItemFrontendDTO();
	        orderItemDTO.setProductName(item.getProducts().getProductName()); // 商品名稱
	        orderItemDTO.setQuantity(item.getQuantity());                    // 購買數量
	        orderItemDTO.setUnitPrice(item.getUnitPrice());                  // 單價
	        orderItemDTO.setDiscount(item.getDiscount());                    // 折扣
	        orderItemDTO.setSubtotal(item.getSubtotal());                    // 小計
	        orderItemDTO.setSpecialPrice(item.getProducts().getSpecialPrice()); // 特別價格
	        return orderItemDTO;
	    }).collect(Collectors.toList());

	    // 轉換訂單主資訊
	    OrderFrontendDTO orderDTO = new OrderFrontendDTO();
	    orderDTO.setOrderId(order.getOrderId());                            // 訂單編號
	    orderDTO.setReceiveName(order.getReceiveName());                    // 收件人姓名
	    orderDTO.setEmail(order.getEmail());                                // 收件人電子信箱
	    orderDTO.setPhoneNumber(order.getPhoneNumber());                    // 收件人電話
	    orderDTO.setAddress(order.getAddress());                            // 收件人地址
	    orderDTO.setOrderStatus(order.getOrderStatus());                    // 訂單狀態
	    orderDTO.setPaymentMethod(order.getPaymentMethod());                // 付款方式
	    orderDTO.setTotalAmount(order.getTotalAmount() != null ? order.getTotalAmount().toPlainString() : "0");            // 總金額
	    orderDTO.setPointsDiscount(order.getPointsDiscount()); // 點數折抵
        orderDTO.setDiscountAmount(order.getDiscountAmount() != null ? order.getDiscountAmount().toPlainString() : "0");         // 折扣金額
        orderDTO.setFinalAmount(order.getFinalAmount() != null ? order.getFinalAmount().toPlainString() : "0");            // 最終金額
        orderDTO.setOrderDate(order.getOrderDate() != null ? formatter.format(order.getOrderDate()) : "");             // 訂單日期
        orderDTO.setUpdatedAt(order.getUpdatedAt() != null ? formatter.format(order.getUpdatedAt()) : "");             // 更新日期
	    orderDTO.setOrderItemsDtos(orderItemDTOs);                          // 細項 DTO
	    return orderDTO;
	}

	// 查詢指定用戶的「所有」訂單（包含訂單細項）
	@Override
	public List<OrderFrontendDTO> getOrdersForFrontendByUserId(Integer userId) {
		// 查詢該用戶的訂單
		List<Order> orders = orderDao.findByUserId(userId);

		// 使用轉換方法轉換為前台的 DTO
		return orders.stream().map(this::convertToFrontendDTO) // 使用 convertToFrontendDTO 方法
				.collect(Collectors.toList());
	}
	
	// 查詢指定用戶的「特定」訂單（包含訂單細項）
	@Override
	public OrderFrontendDTO getOrderDetailForFrontend(Integer userId, Integer orderId) {
	    // 查詢該用戶的訂單
	    Order order = orderDao.findByUserIdAndOrderId(userId, orderId);

	    // 如果訂單不存在，返回 null
	    if (order == null) {
	        return null;
	    }

	    // 使用轉換方法將訂單轉換為前台需要的 DTO
	    return convertToFrontendDTO(order);
	}

	// Cart -> Order
	// 創建訂單的方法
	@Override
	public OrderBackendDTO createOrder(CartTurntoOrderDTO orderRequest) {
		Order order = new Order();

	    try {
	        // 設定訂單的收件人資料
	        MemberForCartFrontendDTO recipient = orderRequest.getRecipient();
	        
	        // 如果 receiveName 為 null，則使用 userName 來作為 receiveName
	        String receiveName = Optional.ofNullable(recipient.getReceiveName()).orElse(recipient.getUserName());
	        order.setReceiveName(receiveName);  // 設置收件人名稱

	        order.setEmail(recipient.getEmail());
	        order.setPhoneNumber(recipient.getPhone());
	        order.setAddress(recipient.getAddress());
	        order.setPaymentMethod(recipient.getPaymentMethod());

	        // 查看傳入的 userid
	        System.out.println("Recipient userid: " + recipient.getUserid());  // print recipient 的 userid
	        System.out.println("Receive Name in createOrder: " + recipient.getReceiveName()); // print recipient 的 receiveName

	        // 設置 userid
	        order.setUserId(recipient.getUserid());  // 確保設置 userid

	        // 設定訂單狀態
	        // 根據付款方式設置訂單狀態
	        if ("信用卡".equals(recipient.getPaymentMethod())) {
	            order.setOrderStatus("未付款");
	        } else {
	            order.setOrderStatus("處理中");
	        }

	        // 初始化總金額和總折扣
	        List<OrderItems> orderItems = new ArrayList<>();
	        //BigDecimal totalAmount = BigDecimal.ZERO;  // 計算訂單的總金額
	        //BigDecimal discountAmount = BigDecimal.ZERO;  // 計算訂單的總折扣
	        
	        for (CartItemFrontendDTO cartItem : orderRequest.getOrderItems()) {
	            OrderItems item = new OrderItems();
	            Optional<Products> productOptional = productDao.findById(cartItem.getProductId());

	            if (productOptional.isPresent()) {
	                Products product = productOptional.get();
	                item.setProducts(product);
	                item.setQuantity(cartItem.getQuantity());
	                
	                
	                // 計算折扣：若有特價則折扣 = 原價 - 特價，若無則為 0
	                BigDecimal unitPrice = product.getPrice(); // 使用商品原價作為單價
	                BigDecimal discount = product.getSpecialPrice() != null && product.getSpecialPrice().compareTo(BigDecimal.ZERO) > 0
	                    ? product.getPrice().subtract(product.getSpecialPrice()) // 折扣 = 原價 - 特價
	                    : BigDecimal.ZERO; // 沒有特價則折扣為 0
	                
	                // 計算小計：(原價 - 折扣) * 數量
	                BigDecimal subtotal = (unitPrice.subtract(discount))
	                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

	                // 更新折扣、單價與小計
	                item.setDiscount(discount);
	                item.setUnitPrice(unitPrice);
	                item.setSubtotal(subtotal);
	                System.out.println("Product: " + cartItem.getProductId());
	                System.out.println("Unit Price: " + unitPrice);
	                System.out.println("Discount: " + discount);
	                System.out.println("Quantity: " + cartItem.getQuantity());
	                System.out.println("Subtotal: " + subtotal);

	                // 累加訂單的總金額（原價 * 數量）
	                //BigDecimal itemTotalAmount = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
	                //totalAmount = totalAmount.add(itemTotalAmount);
	                
	                // 累加訂單的總折扣（每個商品的折扣金額）
	                //BigDecimal itemDiscountAmount = cartItem.getDiscount().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
	                //discountAmount = discountAmount.add(itemDiscountAmount);

	                item.setOrder(order);  // 設定訂單關聯
	                // 在插入前檢查 OrderItems 的值
	                System.out.println("OrderItem: " + item.getUnitPrice() + ", " + item.getDiscount() + ", " + item.getSubtotal());
	                orderItems.add(item);
	            } else {
	                // 如果商品 ID 不存在
	                throw new RuntimeException("Product not found for ID: " + cartItem.getProductId());
	            }
	        }

	        order.setOrderItemsBeans(new HashSet<>(orderItems));
	        
	        
	        // 呼叫 calculateOrderTotal 方法來計算訂單總金額和最終金額
	        calculateOrderTotal(order, orderItems);
	        //System.out.println("Order Data: " + order);
	        
	        // 儲存訂單到資料庫
	        order = orderDao.save(order);
	        
	        // 更新購物車商品狀態為 4（已購買）
	        for (CartItemFrontendDTO cartItem : orderRequest.getOrderItems()) {
	            // 透過 CartItemsDao 查詢這些商品
	            Optional<CartItems> optionalCartItem = cartItemsDao.findByCartItemIdAndIsValid(cartItem.getCartItemId(), 0);
	            if (optionalCartItem.isPresent()) {
	                CartItems item = optionalCartItem.get();
	                item.setIsValid(4);  // 更新商品狀態為 4（已購買）
	                cartItemsDao.save(item);  // 儲存更新後的商品狀態
	            }
	        }
	        
	        // 轉換為 DTO 並返回
	        OrderBackendDTO orderBackendDTO = convertToBackendDTO(order);
	        
	        // 設置訂單的總金額、折扣和最終金額
	        // 將 BigDecimal 轉為 String
	        orderBackendDTO.setTotalAmount(order.getTotalAmount().setScale(0, RoundingMode.HALF_UP).toString());
	        orderBackendDTO.setDiscountAmount(order.getDiscountAmount().setScale(0, RoundingMode.HALF_UP).toString());
	        orderBackendDTO.setFinalAmount(order.getFinalAmount().setScale(0, RoundingMode.HALF_UP).toString());
	        
	        // 轉換為 DTO 並返回
	        return orderBackendDTO;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error creating order: " + e.getMessage());  // 顯示詳細錯誤
	    }
	}
	
	// 更新訂單狀態為支付成功
	public void paymentSuccess(Integer orderId) {
	    Order order = orderDao.findById(orderId)
	            .orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + orderId));
	    
	    // 更新訂單狀態為已付款
	    order.setOrderStatus("已付款");
	    order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));  // 更新時間

	    orderDao.save(order);  // 保存更新後的訂單
	}

}
