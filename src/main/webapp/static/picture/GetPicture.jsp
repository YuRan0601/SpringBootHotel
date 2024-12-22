<%@page import="com.cloudSerenityHotel.attraction_facility.attraction.model.Picture"%>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>圖片資料</title>
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
        <h2>圖片資料</h2>

        <%
            Picture picture = (Picture) request.getAttribute("picture");
            if (picture != null) {
        %>
        <table>
            <tr>
                <th>圖片ID</th>
                <td><%= picture.getImageId() %></td>
            </tr>
            <tr>
                <th>關聯ID</th>
                <td><%= picture.getReferenceId() %></td>
            </tr>
            <tr>
                <th>關聯類型</th>
                <td><%= picture.getReferenceType() %></td>
            </tr>
            <tr>
                <th>圖片連結</th>
                <td><a href="<%= picture.getImageUrl() %>" target="_blank">查看圖片</a></td>
            </tr>
            <tr>
                <th>建立日期</th>
                <td><%= picture.getCreateAt() %></td>
            </tr>
            <tr>
                <th>更新日期</th>
                <td><%= picture.getUpdateAt() %></td>
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
