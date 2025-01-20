package com.cloudSerenityHotel.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.order.dao.OrderDao;
import com.cloudSerenityHotel.order.dao.OrderItemsDao;
import com.cloudSerenityHotel.order.model.OrderBean;
import com.cloudSerenityHotel.order.model.OrderItemsBean;
import com.cloudSerenityHotel.order.service.OrderService;

import jakarta.transaction.Transactional;

// JPA 的 save 方法會根據物件是否有主鍵來自動選擇是執行「新增」還是「更新」。
@Service
@Transactional // 自動交易管理員
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderItemsDao orderItemsDao;

//整筆訂單
		// delete_刪除訂單_依據訂單Id, 參數 Id
		@Override
		public boolean deleteOrderById(Integer orderId) {
			try {
				orderDao.deleteById(orderId);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			//return orderDao.deleteOne(orderId); // 舊Hibernate&自定義方法
		}
	
		// find_查詢全部訂單資料_好多OrderBean物件就是為一個集合
		@Override
		public List<OrderBean> selectAll() {
			return orderDao.findAll();
		}
	
		// find_查詢單筆訂單_依據訂單Id, 參數 Id
		@Override
		public OrderBean selectOrderById(Integer orderId) {
			return orderDao.findById(orderId).orElse(null);
			// .orElse(null)?? 不懂
		}
	
		// 新增必須連同細項一起新增
		// insert_新增訂單
		@Override
		public OrderBean insertOrder(OrderBean orderBean) {
			return orderDao.save(orderBean);
		}
	
		// update_修改訂單
		@Override
		public OrderBean updateOrderById(OrderBean orderBean) {
			return orderDao.save(orderBean);
		}
	
//單純訂單細項
		
		// 刪除單一訂單細項
		@Override
		public boolean deleteOrderItemById(Integer orderItemId) {
			try {
				orderItemsDao.deleteById(orderItemId);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			// return orderItemsDao.deleteOne(orderItemId); // 舊Hibernate&自定義方法
		}
		
		// 為已存在訂單新增訂單細項
		@Override
		public List<OrderItemsBean> insertItemsToExistingOrder(Integer orderId, List<OrderItemsBean> items) {
			// 透過 orderId 查找訂單
			OrderBean order = orderDao.findById(orderId).orElse(null);
			
			if (order != null) {
				for (OrderItemsBean oneItem : items) {
					// 將 OrderBean 設定到 OrderItemsBean
					oneItem.setOrder(order); 
				}
				
				// 使用 saveAll 一次插入所有訂單細項
				return orderItemsDao.saveAll(items);
			}
			
			// 如果找不到對應的訂單，則返回空的列表
			return new ArrayList<>();
		}
		
		// 修改訂單
			//單筆更新
			@Override
			public OrderItemsBean updateOrderItem(OrderItemsBean orderItem) {
				// 單筆細項更新，直接調用 JPA save 方法
			    return orderItemsDao.save(orderItem); // JPA 的 save 方法
			}
			
			//用於批量更新多筆訂單細項
			@Override
			public void updateOrderItems(int orderId, List<OrderItemsBean> orderItems) {
			    // 查詢關聯的 OrderBean
			    OrderBean order = orderDao.findById(orderId)
			            .orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + orderId));
			    // 更新每個訂單細項
			    for (OrderItemsBean item : orderItems) {
			        item.setOrder(order); // 設置關聯的 OrderBean
			        updateOrderItem(item); // 單筆更新
			    }
			}

//訂單&訂單細項 
			
	//返回的是完整的 OrderBean
		//包括訂單的主表數據、細項，以及細項中關聯的產品（Products）
		@Override
		public OrderBean getOrderDetails(Integer orderId) {
		    OrderBean order = orderDao.findById(orderId)
		            .orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + orderId));

		    // 初始化訂單細項
		    Hibernate.initialize(order.getOrderItemsBeans());
		    for (OrderItemsBean item : order.getOrderItemsBeans()) {
		        Hibernate.initialize(item.getProducts()); // 初始化產品屬性
		        System.out.println("產品名稱：" + item.getProducts().getName()); // 檢查商品名稱是否正確
		    }

		    return order;
		}
	
	//返回的是 OrderItemsBean 的列表
		// find_查詢訂單&訂單細項
		@Override
		public List<OrderItemsBean> findOrderItemsByOrderId(Integer orderId) {
			return orderItemsDao.findByOrderOrderId(orderId);
		}
	
		// insert_新增訂單&訂單細項
		@Override
		public OrderBean insertOrderWithItems(OrderBean orderBean, List<OrderItemsBean> items) {
		    // 插入訂單主檔
		    orderBean = orderDao.save(orderBean);

		    // 插入訂單細項
		    for (OrderItemsBean oneItem : items) {
		        oneItem.setOrder(orderBean); // 關聯主檔
		    }
		    orderItemsDao.saveAll(items);

		    // 計算總金額並更新訂單主檔
		    calculateOrderTotal(orderBean, items);
		    orderDao.save(orderBean); // 更新主檔總金額

		    return orderBean;
		}
	
//較複雜邏輯運算
		// 計算訂單總金額
	    public void calculateOrderTotal(OrderBean order, List<OrderItemsBean> items) {
	        BigDecimal totalAmount = BigDecimal.ZERO;
	        BigDecimal discountAmount = BigDecimal.ZERO;

	        for (OrderItemsBean item : items) {
	            BigDecimal itemSubtotal = item.getUnitPrice()
	                    .subtract(item.getDiscount()) // 單價減折扣
	                    .multiply(BigDecimal.valueOf(item.getQuantity())); // 乘以數量
	            totalAmount = totalAmount.add(itemSubtotal); // 累計總金額
	            discountAmount = discountAmount.add(item.getDiscount().multiply(BigDecimal.valueOf(item.getQuantity())));
	        }

	        order.setTotalAmount(totalAmount);
	        order.setDiscountAmount(discountAmount);
	        order.setFinalAmount(totalAmount.subtract(discountAmount)); // 計算最終金額
	    }
	
}
