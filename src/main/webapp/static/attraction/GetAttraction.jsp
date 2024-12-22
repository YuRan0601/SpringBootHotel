<%@page import="com.cloudSerenityHotel.attraction_facility.attraction.model.Attraction"%>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>景點資料</title>
    <style>
        body {
            font-family: "Arial", sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 80%;
            margin: 50px auto;
            padding: 20px;
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            text-align: center;
        }
        h2 {
            font-size: 2em;
            color: #9c70e8; /* 淡紫色 */
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        table th, table td {
            padding: 12px 15px;
            text-align: left;
        }
        table th {
            background-color: #9c70e8; /* 淡紫色 */
            color: white;
            font-weight: bold;
            text-transform: uppercase;
        }
        table td {
            border: 1px solid #dddddd;
            background-color: #f9f9f9;
        }
        table tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        a {
            color: #9c70e8; /* 淡紫色 */
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        .no-data {
            font-size: 1.2em;
            color: #ff5722;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>景點資料</h2>

        <%
            Attraction attraction = (Attraction) request.getAttribute("attraction");
            if (attraction != null) {
        %>
        <table>
            <tr>
                <th>景點名稱</th>
                <td><%= attraction.getName() %></td>
            </tr>
            <tr>
                <th>簡介</th>
                <td><%= attraction.getDescription() %></td>
            </tr>
            <tr>
                <th>地址</th>
                <td><%= attraction.getLocation() %></td>
            </tr>
            <tr>
                <th>營業時間</th>
                <td><%= attraction.getOpeningHours() %></td>
            </tr>
            <tr>
                <th>聯絡方式</th>
                <td><%= attraction.getContactInfo() %></td>
            </tr>
            <tr>
                <th>景點類型</th>
                <td><%= attraction.getTypeId() %></td>
            </tr>
            <tr>
                <th>圖片連結</th>
                <td><a href="<%= attraction.getImageUrl() %>" target="_blank">查看圖片</a></td>
            </tr>
            <tr>
                <th>建立日期</th>
                <td><%= attraction.getCreateAt() %></td>
            </tr>
            <tr>
                <th>更新日期</th>
                <td><%= attraction.getUpdateAt() %></td>
            </tr>
        </table>
        <%
            } else {
        %>
        <p class="no-data">查無資料！</p>
        <%
            }
        %>
    </div>
</body>
</html>
