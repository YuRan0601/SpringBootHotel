<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>訂單詳情</title>
    <link rel="stylesheet" href="/CloudSerenityHotel/static/order/css/xxx.css" />
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #fdf5e6;
            text-align: center;
        }

        h1, h2 {
            color: #1162be;
        }

        fieldset {
            border: 1px solid #ccc;
            padding: 20px;
            margin: 20px auto;
            width: 70%;
            background-color: #fff;
        }

        legend {
            font-size: 1.5em;
            font-weight: bold;
            color: #1162be;
        }

        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
        }

        table th, table td {
            padding: 10px;
            border: 1px solid #ccc;
            text-align: center;
        }

        .button {
            background-color: #ECF5FF;
            color: #1162be;
            padding: 10px 20px;
            font-weight: bold;
            border-radius: 10px;
            cursor: pointer;
            border: none;
            margin-top: 10px;
        }

        .button:hover {
            background-color: #357ac8;
            color: #ffffff;
        }

        input[type="text"] {
            padding: 8px;
            border-radius: 5px;
            border: 1px solid #ccc;
            width: 300px;
        }
    </style>
</head>

<body>

    <h1>訂單詳情</h1>

    <!-- 顯示訂單基本資訊 -->
    <fieldset>
        <legend>訂單資料</legend>
        <table>
            <tr>
                <td>訂單ID</td>
                <td><input type="text" disabled value="${order.orderId}"></td>
            </tr>
            <tr>
                <td>使用者ID</td>
                <td><input type="text" disabled value="${order.userId}"></td>
            </tr>
            <tr>
                <td>收件人</td>
                <td><input type="text" disabled value="${order.receiveName}"></td>
            </tr>
            <tr>
                <td>email</td>
                <td><input type="text" disabled value="${order.email}"></td>
            </tr>
            <tr>
                <td>電話號碼</td>
                <td><input type="text" disabled value="${order.phoneNumber}"></td>
            </tr>
            <tr>
                <td>收貨地址</td>
                <td><input type="text" disabled value="${order.address}"></td>
            </tr>
            <tr>
                <td>訂單狀態</td>
                <td><input type="text" disabled value="${order.orderStatus}"></td>
            </tr>
            <tr>
                <td>付款方式</td>
                <td><input type="text" disabled value="${order.paymentMethod}"></td>
            </tr>
            <tr>
                <td>總金額</td>
                <td><input type="text" disabled value="${order.totalAmount}"></td>
            </tr>
            <tr>
                <td>點數折抵</td>
                <td><input type="text" disabled value="${order.pointsDiscount}"></td>
            </tr>
            <tr>
                <td>折扣金額</td>
                <td><input type="text" disabled value="${order.discountAmount}"></td>
            </tr>
            <tr>
                <td>最終金額</td>
                <td><input type="text" disabled value="${order.finalAmount}"></td>
            </tr>
            
             <!-- <tr>
                <td>訂單創建日期</td>
                <td><input type="text" disabled value="${order.orderDate}"></td>
            </tr>-->
            <tr>
                <td>訂單更新編輯日期</td>
                <td><input type="text" disabled value="${order.updatedAt}"></td>
            </tr>
        </table>
    </fieldset>

    <br />
    <!-- 顯示返回訂單列表按鈕 -->
    <form action="/CloudSerenityHotel/Order/findAllOrders" method="post">
        <button type="submit" class="button">返回訂單列表</button>
    </form>

</body>
</html>
