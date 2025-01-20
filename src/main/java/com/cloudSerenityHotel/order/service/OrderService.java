package com.cloudSerenityHotel.order.service;

import java.util.List;

import com.cloudSerenityHotel.order.model.OrderBean;
import com.cloudSerenityHotel.order.model.OrderItemsBean;


public interface OrderService {

	//整筆訂單
			// delete_刪除訂單_依據訂單Id, 參數 Id
			boolean deleteOrderById(Integer orderId);

			// find_查詢全部訂單資料_好多OrderBean物件就是為一個集合
			List<OrderBean> selectAll();
			
			// find_查詢單筆訂單_依據訂單Id, 參數 Id
			OrderBean selectOrderById(Integer orderId);
			
			// 新增必須連同細項一起新增
			// insert_新增訂單
			OrderBean insertOrder(OrderBean orderBean);
			
			// update_修改訂單
			OrderBean updateOrderById(OrderBean orderBean); 
			
	//單純訂單細項
			// 為已存在訂單新增訂單細項
			List<OrderItemsBean> insertItemsToExistingOrder(Integer orderId, List<OrderItemsBean> items);
		    
		    // 修改單一訂單細項
		    OrderItemsBean updateOrderItem(OrderItemsBean orderItem);
		    
		    // 刪除單一訂單細項
		    boolean deleteOrderItemById(Integer orderItemId);

    //訂單&訂單細項 
		    
		    /*
		     * 1.) 屬於 Service 層，負責處理業務邏輯，是連接 Controller 和 DAO 的橋樑。
		     * 2.) 方法名稱如 findOrderItemsByOrderId 代表一個業務邏輯功能。
		     * 3.) 它的責任是「提供給 Controller 符合業務需求的功能」，會基於 DAO 做更多的邏輯處理（如額外計算或驗證等）。 
		     */
		    
		    //包括訂單的主表數據、細項，以及細項中關聯的產品（Products）
		    OrderBean getOrderDetails(Integer orderId);
		    
			// find_查詢訂單&訂單細項_OrderItemsDao(interface)有相同名稱的方法
			List<OrderItemsBean> findOrderItemsByOrderId(Integer orderId);
			
			// insert_新增訂單&訂單細項
			OrderBean insertOrderWithItems(OrderBean orderBean, List<OrderItemsBean> items);

			void updateOrderItems(int orderId, List<OrderItemsBean> orderItems);
			
			// update_更新訂單和訂單細項
			//OrderBean updateOrderWithItems(OrderBean orderBean, List<OrderItemsBean> items);
			
			
    //較複雜邏輯運算
			void calculateOrderTotal(OrderBean order, List<OrderItemsBean> items);

			
			
		    
}
