package com.cloudSerenityHotel.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cloudSerenityHotel.order.dao.impl.OrderDaoImpl;
import com.cloudSerenityHotel.order.dao.impl.OrderItemsDaoImpl;
import com.cloudSerenityHotel.order.model.OrderBean;
import com.cloudSerenityHotel.order.model.OrderItemsBean;
import com.cloudSerenityHotel.order.service.OrderService;

public class OrderServiceImpl implements OrderService{

	private OrderDaoImpl orderDaoImpl;
	private OrderItemsDaoImpl orderItemsDaoImpl;

    public OrderServiceImpl() {
        this.orderDaoImpl = new OrderDaoImpl();
        this.orderItemsDaoImpl = new OrderItemsDaoImpl();
    }

//整筆訂單
		// delete_刪除訂單_依據訂單Id, 參數 Id
		@Override
		public boolean deleteOrderById(Integer orderId) {
			return orderDaoImpl.deleteOne(orderId);
		}
	
		// find_查詢全部訂單資料_好多OrderBean物件就是為一個集合
		@Override
		public List<OrderBean> selectAll() {
			return orderDaoImpl.selectAll();
		}
	
		// find_查詢單筆訂單_依據訂單Id, 參數 Id
		@Override
		public OrderBean selectOrderById(Integer orderId) {
			return orderDaoImpl.selectById(orderId);
		}
	
		// 新增必須連同細項一起新增
		// insert_新增訂單
		@Override
		public OrderBean insertOrder(OrderBean orderBean) {
			return orderDaoImpl.insert(orderBean);
		}
	
		// update_修改訂單
		@Override
		public OrderBean updateOrderById(OrderBean orderBean) {
			return orderDaoImpl.updateOne(orderBean);
		}
	
//單純訂單細項
		// 為已存在訂單新增訂單細項
		@Override
		public List<OrderItemsBean> insertItemsToExistingOrder(Integer orderId, List<OrderItemsBean> items) {
			List<OrderItemsBean> insertItems = new ArrayList<>();
			// 使用傳入的 orderItems 列表，而不是 insertItems 列表
			for (OrderItemsBean oneItem : insertItems) {
				// 設置每個訂單項目的 orderId
				oneItem.setOrderId(orderId);
				// 插入並返回結果
				OrderItemsBean insert_item = orderItemsDaoImpl.insert(oneItem);
				// 收集所有插入的項目
				insertItems.add(insert_item);
			}
			// 返回所有插入的訂單細項
			return insertItems;
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
			return orderItemsDaoImpl.deleteOne(orderItemId);
		}

//訂單&訂單細項 
		// find_查詢訂單&訂單細項
		@Override
		public List<OrderItemsBean> findOrderItemsByOrderId(Integer orderId) {
			return orderItemsDaoImpl.selectOrderItemsByOrderId(orderId);
		}
	
		// insert_新增訂單&訂單細項
		@Override
		public OrderBean insertOrderWithItems(OrderBean orderBean, List<OrderItemsBean> items) {
			// 插入訂單並返回訂單 ID
			// 假設返回的是包含生成的 orderId 的完整 OrderBean
			orderBean = orderDaoImpl.insert(orderBean);
			
			// 插入訂單項目並將 orderId 設置給每個訂單細項
			for (OrderItemsBean oneItem : items) {
				// 設置 orderId
				oneItem.setOrderId(orderBean.getOrderId());
				// 插入訂單細項
				orderItemsDaoImpl.insert(oneItem);
				//System.out.println("Inserted OrderId: " + orderBean.getOrderId());
			}
			 // 返回插入後的完整訂單資料，包含所有訂單項目
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
			// TODO Auto-generated method stub
			return null;
		}
		
		//未來有折扣需求
		
		
		// 總金額屬於訂單邏輯，由 List<OrderItemsBean> 負責
		@Override
		public BigDecimal calculateOrderFinalAmount(List<OrderItemsBean> items) {
			// TODO Auto-generated method stub
			return null;
		}
	
}
