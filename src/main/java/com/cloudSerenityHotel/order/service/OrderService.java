package com.cloudSerenityHotel.order.service;

import java.math.BigDecimal;
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
		    //OrderItemsBean updateOrderItem(OrderItemsBean orderItems);
		    
		    // 刪除單一訂單細項
		    boolean deleteOrderItemById(Integer orderItemId);

    //訂單&訂單細項 
			// find_查詢訂單&訂單細項
			List<OrderItemsBean> findOrderItemsByOrderId(Integer orderId);
			
			// insert_新增訂單&訂單細項
			OrderBean insertOrderWithItems(OrderBean orderBean, List<OrderItemsBean> items);
			
			// update_更新訂單和訂單細項
			//OrderBean updateOrderWithItems(OrderBean orderBean, List<OrderItemsBean> items);
			
			
    //較複雜邏輯運算			
			// 小計屬於商品邏輯，由 OrderItemsBean 負責
			OrderItemsBean calculateSubTotal(OrderItemsBean orderItems); // 計算商品小計
			
			//未來有折扣需求
			//int calculateOrderFinalAmountWithDiscount(List<OrderItemsBean> items, Integer discountAmount);
			
			// 總金額屬於訂單邏輯，由 List<OrderItemsBean> 負責
			BigDecimal calculateOrderFinalAmount(List<OrderItemsBean> items); // 計算訂單總金額
		    
}
