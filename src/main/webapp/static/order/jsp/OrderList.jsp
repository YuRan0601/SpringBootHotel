<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
	<meta charset="UTF-8">
	<title>訂單列表</title>
	
	<link rel="stylesheet"
		href="/CloudSerenityHotel/static/order/css/OrderList.css" />
	
	<!-- 引入 jQuery -->
	<script src="https://code.jquery.com/jquery-3.7.1.js"
		integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
		crossorigin="anonymous"></script>
	
	<!-- 引入 Bootstrap CSS -->
	<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
		rel="stylesheet" />
	
	<!-- 引入 Bootstrap Icons -->
	<link
		href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
		rel="stylesheet" />
	
	<!-- 引入 Bootstrap JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	
	<!-- 引入aside.css -->
	<link rel="stylesheet" href="/CloudSerenityHotel/static/common/aside.css" />
	
	<script>
			$(function() {
				$(".aside").load("/CloudSerenityHotel/static/common/aside.html");
	
			});
	</script>
	
	<style>
		/* 主要內容的樣式，假設class名為content */
		/*.content {
			margin-left: 300px
		}*/
	</style>

</head>

<body>
	<aside class="aside"></aside>

	<div class="content">
		<h1>訂單管理</h1>

		<!-- 顯示訊息 -->
		<c:if test="${not empty message}">
			<div class="alert alert-success">${message}</div>
		</c:if>

		<!-- 查詢條件輸入框 -->
		<div class="search-bar">
			<form action="/CloudSerenityHotel/Order/findOrderById" method="get">
				<label for="searchOrderId">查詢訂單編號：</label> <input type="text"
					name="orderId" id="searchOrderId" placeholder="輸入訂單編號">
				<button class="button" type="submit">查詢</button>
			</form>
			<div class="action-buttons">
				<!-- 新增訂單按鈕，並與搜尋框有間距 -->
				<a href="/CloudSerenityHotel/static/order/html/InsertOrder.html"
					class="button">新增訂單</a>
				<!-- 修改訂單按鈕，並與搜尋框有間距 -->
				<a href="/CloudSerenityHotel/static/order/html/UpdateOrder.html"
					class="button">修改訂單</a>
			</div>
		</div>

		<!-- 訂單列表顯示 -->
		<table>
			<thead>
				<tr>
					<th>訂單編號</th>
					<th>訂單狀態</th>
					<th>付款方式</th>
					<th>折扣金額</th>
					<th>最終金額</th>
					<th>訂單日期</th>
					<th>更新日期</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<!-- 使用 JSTL 顯示訂單資料 -->
				<c:forEach var="order" items="${orders}" varStatus="s">
					<tr>
						<td>${order.orderId}</td>
						<td>${order.orderStatus}</td>
						<td>${order.paymentMethod}</td>
						<td>${order.discountAmount}</td>
						<td>${order.finalAmount}</td>
						<td>${order.orderDate}</td>
						<td>${order.updatedAt}</td>
						<td><a
							href="/CloudSerenityHotel/Order/findOrderById?orderId=${order.orderId}"
							class="button">查看詳情</a> <!-- 刪除訂單 -->
							<form action="/CloudSerenityHotel/Order/${order.orderId}"
								method="post" style="display: inline;">
								<!-- 使用 hidden 欄位模擬 DELETE 請求 -->
								<input type="hidden" name="_method" value="DELETE">
								<button type="submit" class="button"
									onclick="return confirm('確認刪除此訂單?');">刪除</button>
							</form></td>
						<c:set var="num" value="${s.count}" />
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<h3>共${num}筆訂單資料</h3>
		<!-- 訂單詳情彈窗 -->
		<div id="orderDetailModal" style="display: none;">
			<h2>訂單詳細資訊</h2>
			<p id="orderDetailContent"></p>
			<button onclick="closeModal()">關閉</button>
		</div>
	</div>

	<script>
		// 用來關閉彈窗的函數
		function closeModal() {
			document.getElementById('orderDetailModal').style.display = 'none';
		}
	</script>
</body>
</html>