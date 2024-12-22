package com.cloudSerenityHotel.order.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cloudSerenityHotel.base.BaseController;
import com.cloudSerenityHotel.order.model.OrderBean;
import com.cloudSerenityHotel.order.model.OrderItemsBean;
import com.cloudSerenityHotel.order.service.impl.OrderServiceImpl;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


// 進入點URL -> http://localhost:8080/CloudSerenityHotel/Order/findAllOrders
@WebServlet("/Order/*") // 這裡設置的是所有 "/Order/" 開頭的路徑
public class OrderController extends BaseController {
	private static final long serialVersionUID = 1L;

	private OrderServiceImpl orderServiceImpl;

	public OrderController() {
		super();
		// 這個超重要, 當初就是沒有這個DB資料一直出不來
		this.orderServiceImpl = new OrderServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 確保解析 action 參數，並根據 action 進行對應的處理
		String path = request.getPathInfo(); // 獲取URL後的部分，例如 '/findOrderById'

		if ("/findOne".equals(path)) {
		    findOrderById(request, response); // 查詢單筆訂單
		} else if ("/findAll".equals(path)) {
		    findAllOrders(request, response); // 查詢所有訂單
		} else if ("/add".equals(path)) {
		    insertOrder(request, response); // 新增訂單
		} else if ("/delete".equals(path)) {
		    deleteOrder(request, response); // 刪除訂單
		} else if ("/update".equals(path)) {
		    updateOrder(request, response); // 更新訂單
		} else {
		    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid action path");
		}
		
		// 取得 action 參數
		/*String action = request.getParameter("action");
		// 根據 action 參數來決定要執行哪個方法
		if ("delete".equals(action)) {
			deleteOrder(request, response);
		} else if ("view".equals(action)) {
			findOrderById(request, response);
		} else if ("add".equals(action)) {
			insertOrder(request, response);
		} else if ("update".equals(action)) {
			updateOrder(request, response);
		} else {
			findAllOrders(request, response);
		}*/
	}

	// 查詢所有訂單
	public void findAllOrders(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession httpSession = request.getSession();
		// 調用 service 層來取得所有訂單
		try {
			List<OrderBean> orders = orderServiceImpl.selectAll();
			System.out.println("Orders size: " + orders.size()); // 打印資料數量來檢查是否有資料
			// 加到 http session 內
			httpSession.setAttribute("orders", orders); // 把結果放到 request 屬性中，供 JSP 頁面使用??

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 將請求和回應轉發（forward）到 OrderList.jsp 頁面，讓它來處理後續的顯示邏輯。
		RequestDispatcher rd = request.getRequestDispatcher("/static/order/jsp/OrderList.jsp");
		rd.forward(request, response);
	}

	// 查詢單筆訂單
	public void findOrderById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession httpSession = request.getSession();
		// 從 request 取得訂單 ID(並從 String->Integer)
		int orderId = Integer.parseInt(request.getParameter("orderId"));

		// 查詢訂單
		OrderBean order = orderServiceImpl.selectOrderById(orderId);
		System.out.println("Order ID from order bean: " + order.getOrderId());

		// 查詢訂單細項
		List<OrderItemsBean> orderItems = orderServiceImpl.findOrderItemsByOrderId(orderId);

		// 將資料存放到 request
		// 單筆訂單
		httpSession.setAttribute("order", order);
		// 訂單項目
		httpSession.setAttribute("orderItems", orderItems);

		// 將請求和回應轉發（forward）到 OrderDetail.jsp 頁面，讓它來處理後續的顯示邏輯。
		RequestDispatcher rd = request.getRequestDispatcher("/static/order/jsp/OrderDetail.jsp");
		rd.forward(request, response);

	}

	// 刪除訂單
	public void deleteOrder(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession httpSession = request.getSession();
		// 從 request 取得訂單 ID
		String orderIdStr = request.getParameter("orderId");
		if (orderIdStr != null && !orderIdStr.trim().isEmpty()) {
			try {
				// (並從 String->Integer)
				int orderId = Integer.parseInt(orderIdStr);
				orderServiceImpl.deleteOrderById(orderId);
				// 設置刪除成功訊息
				httpSession.setAttribute("message", "訂單已成功刪除");
				// 刪除後回到訂單列表頁面
				response.sendRedirect(request.getContextPath() + "/Order/findAllOrders");
			} catch (NumberFormatException e) {
				// response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無效的訂單 ID");
			}

		} else {
			// response.sendError(HttpServletResponse.SC_BAD_REQUEST, "訂單 ID 不可為空");
		}
	}

	// 新增訂單
	public void insertOrder(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession httpSession = request.getSession();
		try {
			// 創建訂單實例
			OrderBean order = new OrderBean();
			// 設置訂單屬性（例如用戶 ID）
			String userIdStr = request.getParameter("userId");
			if (userIdStr != null && !userIdStr.isEmpty()) {
				// 少了.trim()
				// if (userIdStr != null && !userIdStr.trim().isEmpty()) {
				int userId = Integer.parseInt(userIdStr);
				order.setUserId(userId);
			}
			order.setReceiveName(request.getParameter("receiveName"));
			order.setEmail(request.getParameter("email"));
			order.setPhoneNumber(request.getParameter("phoneNumber"));
			order.setAddress(request.getParameter("address"));
			order.setOrderStatus(request.getParameter("orderStatus"));
			order.setPaymentMethod(request.getParameter("paymentMethod"));
			// 設置金額相關欄位
			order.setTotalAmount(new BigDecimal(request.getParameter("totalAmount")));
			order.setDiscountAmount(new BigDecimal(request.getParameter("discountAmount")));
			order.setFinalAmount(new BigDecimal(request.getParameter("finalAmount")));

			// 設置時間
			LocalDateTime now = LocalDateTime.now();
			Timestamp timestamp = Timestamp.valueOf(now);
			order.setOrderDate(timestamp);
			order.setUpdatedAt(timestamp);
			// 創建訂單細項並設置與訂單的關聯
			List<OrderItemsBean> orderItems = createOrderItemsFromRequest(request, order);
			// 呼叫 Service 存入訂單和細項
			OrderBean saveOrderAndItems = orderServiceImpl.insertOrderWithItems(order, orderItems);

			// 將資料存放到 HttpSession
			// 單筆訂單
			httpSession.setAttribute("order", order); // 已包含生成的 orderId
			// 訂單項目
			httpSession.setAttribute("orderItems", orderItems); // Hibernate 可自動填充細項

			// 將請求和回應轉發（forward）到 OrderDetail.jsp 頁面，讓它來處理後續的顯示邏輯。
			RequestDispatcher rd = request.getRequestDispatcher("/static/order/jsp/OrderDetail.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "新增訂單失敗");
		}
	}

// 從請求中創建訂單細項_不知道變成Hibernate此處是否需要做變動???!=====================================
	private List<OrderItemsBean> createOrderItemsFromRequest(HttpServletRequest request, OrderBean order) {
		List<OrderItemsBean> orderItemsList = new ArrayList<>();
		String[] productIds = request.getParameterValues("productId");
		String[] quantities = request.getParameterValues("quantity");
		String[] unitPrices = request.getParameterValues("unitPrice");
		String[] discounts = request.getParameterValues("discount");

		for (int i = 0; i < productIds.length; i++) {
			OrderItemsBean item = new OrderItemsBean();
			item.setProductId(Integer.parseInt(productIds[i]));
			item.setQuantity(Integer.parseInt(quantities[i]));
			item.setUnitPrice(new BigDecimal(unitPrices[i]));
			item.setDiscount(new BigDecimal(discounts[i]));
			item.setSubtotal(
					item.getUnitPrice().subtract(item.getDiscount()).multiply(BigDecimal.valueOf(item.getQuantity())));
			item.setOrder(order); // 設置關聯關係
			orderItemsList.add(item);
		}
		return orderItemsList;
	}

// 以下兩個方法不太懂意義何在???!===================================================================
	// 修改訂單(不能改細項)
	public void updateOrder(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession httpSession = request.getSession();
		try {
			 // 取得前端表單的資料
	        int orderId = Integer.parseInt(request.getParameter("orderId"));
	        int userId = Integer.parseInt(request.getParameter("userId")); // 直接解析，假設資料OK
	        String receiveName = request.getParameter("receiveName");
	        String email = request.getParameter("email");
	        String phoneNumber = request.getParameter("phoneNumber");
	        String address = request.getParameter("address");
	        String orderStatus = request.getParameter("orderStatus");
	        String paymentMethod = request.getParameter("paymentMethod");
	        BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount")); // 假設資料OK
	        int pointsDiscount = Integer.parseInt(request.getParameter("pointsDiscount"));
	        BigDecimal discountAmount = new BigDecimal(request.getParameter("discountAmount"));
	        BigDecimal finalAmount = new BigDecimal(request.getParameter("finalAmount"));

			// 查詢現有的訂單資料，保持 orderDate 原來的時間
			OrderBean orderToUpdate = orderServiceImpl.selectOrderById(orderId); // 假設有這樣的查詢方法
			
			// 取得現有的創建時間 (orderDate)
			Timestamp orderDate = orderToUpdate.getOrderDate();

			// 創建更新後的訂單物件，保持 orderDate 不更新
			OrderBean order = new OrderBean(orderId, userId, receiveName, email, phoneNumber, address, orderStatus,
					paymentMethod, totalAmount, pointsDiscount, discountAmount, finalAmount, orderDate,
					new Timestamp(System.currentTimeMillis()));

			// 呼叫 Service 層進行訂單更新
			orderServiceImpl.updateOrderById(order); // 更新訂單資料
			
			// 更新訂單後，根據 orderId 查詢訂單細項
			List<OrderItemsBean> orderItems = orderServiceImpl.findOrderItemsByOrderId(orderId);
			
			// 設置訂單和訂單細項物件到 request，讓 JSP 頁面可以訪問
			httpSession.setAttribute("order", order);
			httpSession.setAttribute("orderItems", orderItems); // Hibernate 可自動填充細項

			// 將請求和回應轉發（forward）到 OrderDetail.jsp 頁面，讓它來處理後續的顯示邏輯。
			RequestDispatcher rd = request.getRequestDispatcher("/static/order/jsp/OrderDetail.jsp");
			rd.forward(request, response);
		} catch (NumberFormatException e) {
			// 捕獲異常，返回給使用者錯誤訊息
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "輸入的數字格式不正確！");
		}
	}

	/*
	 * protected void doPost(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException { // TODO Auto-generated
	 * method stub doGet(request, response); }
	 */

}
