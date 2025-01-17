package com.cloudSerenityHotel.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
	
		// 修改單一訂單細項
		/*@Override
		public OrderItemsBean updateOrderItem(OrderItemsBean orderItem) {
			// TODO Auto-generated method stub
			return null;
		}*/
		
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
			//return orderItemsDao.deleteOne(orderItemId); // 舊Hibernate&自定義方法
		}

//訂單&訂單細項 
		// find_查詢訂單&訂單細項
		@Override
		public List<OrderItemsBean> findOrderItemsByOrderId(Integer orderId) {
			return orderItemsDao.findByOrderOrderId(orderId);
		}
	
		// insert_新增訂單&訂單細項
		@Override
		public OrderBean insertOrderWithItems(OrderBean orderBean, List<OrderItemsBean> items) {
			// 插入訂單，並返回已設定的 orderId
			// 使用 save 插入訂單，會自動生成 orderId
			orderBean = orderDao.save(orderBean);
			
			// 為每個訂單細項設置 orderId 並一次性插入所有細項
			for (OrderItemsBean oneItem : items) {
				// 設置 order 實體
				// 設置 OrderBean 實體
				oneItem.setOrder(orderBean);
			}
			
			// 使用 saveAll 一次插入所有訂單細項
			orderItemsDao.saveAll(items);
			//System.out.println("Inserted OrderId: " + orderBean.getOrderId());
			
			// 返回插入後的訂單資料，包含所有訂單細項
			return orderBean;
		}
	
		// update_更新訂單和訂單細項_待處理
		/*@Override
		public OrderBean updateOrderWithItems(OrderBean orderBean, List<OrderItemsBean> items) {
			orderDaoImpl.updateOne(orderBean);
			
			for (OrderItemsBean oneItem : items) {
				orderItemsDaoImpl.
			}
			return null;
		}*/
	
//較複雜邏輯運算
		// 小計屬於商品邏輯，由 OrderItemsBean 負責
		@Override
		public OrderItemsBean calculateSubTotal(OrderItemsBean orderItems) {
			// 確保單價和數量不為 null
			if (orderItems.getUnitPrice() != null && orderItems.getQuantity() != null) {
				// 獲取單價
				BigDecimal unitPrice = orderItems.getUnitPrice();
				// 如果折扣為 null，默認設置為 0
				BigDecimal discount = orderItems.getDiscount() != null ? orderItems.getDiscount() : BigDecimal.ZERO;
				// 獲取購買數量
				int quantity = orderItems.getQuantity();
				
				// 計算小計: (單價 - 折扣) * 數量
				BigDecimal subtotal = unitPrice.subtract(discount).multiply(BigDecimal.valueOf(quantity));
				// 更新小計屬性
				orderItems.setSubtotal(subtotal);
				
			} else {
				// 如果數據不完整（例如單價或數量為 null），設置小計為 0
				orderItems.setSubtotal(BigDecimal.ZERO);
			}
			// 返回更新後的對象
			return orderItems;
		}
		
		// 總金額屬於訂單邏輯，由 List<OrderItemsBean> 負責
		@Override
		public BigDecimal calculateOrderFinalAmount(List<OrderItemsBean> items) {
			// 初始化總金額為 0
			BigDecimal finalAmount = BigDecimal.ZERO;
			// 遍歷所有訂單項
			for (OrderItemsBean item : items) {
				// 如果小計不為 null，累加到總金額
				if (item.getSubtotal() != null) {
					finalAmount = finalAmount.add(item.getSubtotal());
				}
			}
			// 返回計算出的總金額
			return finalAmount;
		}
		
		//未來有折扣需求
	
}
