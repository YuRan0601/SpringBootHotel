<%@page import="com.cloudSerenityHotel.attraction_facility.facility.model.Facility"%>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>設施資料</title>
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
        <h2>設施資料</h2>

        <%
            Facility facility = (Facility) request.getAttribute("facility");
            if (facility != null) {
        %>
        <table>
            <tr>
                <th>設施名稱</th>
                <td><%= facility.getName() %></td>
            </tr>
            <tr>
                <th>簡介</th>
                <td><%= facility.getDescription() %></td>
            </tr>
            <tr>
                <th>設施位置</th>
                <td><%= facility.getLocation() %></td>
            </tr>
            <tr>
                <th>開放時間</th>
                <td><%= facility.getAvailabilityHours() %></td>
            </tr>
            <tr>
                <th>建立日期</th>
                <td><%= facility.getCreateAt() %></td>
            </tr>
            <tr>
                <th>更新日期</th>
                <td><%= facility.getUpdateAt() %></td>
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
