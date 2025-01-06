<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>

<head>
<title>訂單詳情</title>
<style>
body {
	margin-left: 500px;
}
</style>
</head>

<body style="background-color: #fdf5e6">
	<h2>訂單詳情</h2>

	<!-- 顯示訂單基本資訊 -->
	<div>
		<table>
			<tr>
				<td>訂單ID
				<td><input type="text" disabled value="${order.orderId}"
					name="orderId">
			<tr>
				<td>使用者ID
				<td><input type="text" disabled value="${order.userId}">
			<tr>
				<td>收件人
				<td><input type="text" disabled value="${order.receiveName}">
			<tr>
				<td>email
				<td><input type="text" disabled value="${order.email}">
			<tr>
				<td>電話號碼
				<td><input type="text" disabled value="${order.phoneNumber}">
			<tr>
				<td>收貨地址
				<td><input type="text" disabled value="${order.address}">
			<tr>
				<td>訂單狀態
				<td><input type="text" disabled value="${order.orderStatus}">
			<tr>
				<td>付款方式
				<td><input type="text" disabled value="${order.paymentMethod}">
			<tr>
				<td>總金額
				<td><input type="text" disabled value="${order.totalAmount}">
			<tr>
				<td>點數折抵(不確定的功能 先保留)
				<td><input type="text" disabled value="${order.pointsDiscount}">
			<tr>
				<td>折扣金額
				<td><input type="text" disabled value="${order.discountAmount}">
			<tr>
				<td>最終金額
				<td><input type="text" disabled value="${order.finalAmount}">
			<tr>
				<td>訂單創建日期
				<td><input type="text" disabled value="${order.orderDate}">
			<tr>
				<td>訂單更新編輯日期
				<td><input type="text" disabled value="${order.updatedAt}">
		</table>
		<!-- 顯示修改確認按鈕 -->
		<!-- <form action="/cloudSerenityHotel/Order/updateOrder" method="post">
			<input type="hidden" name="orderId" value="${order.orderId}">
			<button type="submit">修改訂單資料</button>
		</form>-->
		<!-- 刪除訂單 -->
            <form action="/CloudSerenityHotel/Order/${order.orderId}" method="post" style="display:inline;">
                <!-- 使用 hidden 欄位模擬 DELETE 請求 -->
                <input type="hidden" name="_method" value="DELETE">
                <button type="submit" class="button" onclick="return confirm('確認刪除此訂單?');">刪除</button>
            </form>
		<!-- 顯示刪除確認按鈕 
		<form action="/CloudSerenityHotel/Order/deleteOrder" method="post" onsubmit="return confirm('確認刪除此訂單?');">
			<input type="hidden" name="orderId" value="${order.orderId}">
			<button type="submit">確認刪除</button>
		</form>
		-->
	</div>
	<!-- 顯示訂單項目 -->
	<h3>訂單項目</h3>
	<table border="1">
		<thead>
			<tr>
				<th>訂單內細項編號</th>
				<th>產品ID</th>
				<th>數量</th>
				<th>單價</th>
				<th>折扣</th>
				<th>小計</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${orderItems}">
				<tr>
					<td>${item.orderitemId}</td>
					<td>${item.productId}</td>
					<td>${item.quantity}</td>
					<td>${item.unitPrice}</td>
					<td>${item.discount}</td>
					<td>${item.subtotal}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<br />
	<!-- 顯示返回或其他操作 -->
	<form action="/CloudSerenityHotel/Order/findAllOrders" method="get">
		<button type="submit">返回訂單列表</button>
	</form>
</body>

</html>