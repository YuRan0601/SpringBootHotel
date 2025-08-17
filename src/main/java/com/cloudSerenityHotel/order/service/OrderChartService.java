package com.cloudSerenityHotel.order.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cloudSerenityHotel.order.dao.OrderDao;

/**
 * 專門給chart.js 用的Service
 */
@Service
@Transactional
public class OrderChartService {
	@Autowired
	private OrderDao orderDao; // 操作訂單主檔的 DAO
	
	// 訂單總數
    public Long getTotalOrderCount() {
        return orderDao.count();
    }

    // 每月訂單數量 (回傳 [{month: "2025-01", bookingCount: 20}, ...])
    public List<Map<String, Object>> findMonthlyOrderCount() {
//        return orderDao.findMonthlyOrderCount();
        return null;
    }

    // 訂單狀態分布 (回傳 [{status: "已完成", count: 50}, ...])
    public List<Map<String, Object>> findOrderStatusDistribution() {
//        return orderDao.findOrderStatusDistribution();
    	return null;
    }

    // 熱銷商品 (回傳 [{productName: "紅茶", sales: 100}, ...])
    public List<Map<String, Object>> findTopProductSales() {
//        return orderDao.findTopProductSales();
    	return null;
    }
}