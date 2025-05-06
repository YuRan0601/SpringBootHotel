<%@page import="com.cloudSerenityHotel.attraction_facility.facility.model.Facility"%>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>設施資料</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }
        h2 {
            color: #9b59b6; /* 淡紫色 */
            font-size: 36px;
            text-align: center;
            margin-top: 50px;
        }
        .container {
            width: 80%;
            margin: auto;
            background-color: #ffffff;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 30px;
        }
        th, td {
            padding: 12px;
            text-align: center;
            font-size: 16px;
        }
        th {
            background-color: #9b59b6; /* 淡紫色 */
            color: white;
        }
        td {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) td {
            background-color: #f9f9f9;
        }
        tr:hover td {
            background-color: #f1f1f1;
        }
        a {
            color: #9b59b6; /* 淡紫色 */
            text-decoration: none;
            font-weight: bold;
        }
        a:hover {
            text-decoration: underline;
        }
        .total-count {
            font-size: 18px;
            color: #555;
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>設施資料</h2>
        <table>
            <thead>
                <tr>
                    <th>設施名稱</th>
                    <th>簡介</th>
                    <th>位置</th>
                    <th>開放時間</th>
                    <th>建立日期</th>
                    <th>更新日期</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    List<Facility> facilities = (ArrayList<Facility>) request.getAttribute("facilityList"); 
                    for (Facility facility : facilities) { 
                %>
                <tr>
                    <td><%= facility.getName() %></td>
                    <td><%= facility.getDescription() %></td>
                    <td><%= facility.getLocation() %></td>
                    <td><%= facility.getAvailabilityHours() %></td>
                    <td><%= facility.getCreateAt() %></td>
                    <td><%= facility.getUpdateAt() %></td>
                </tr>
                <% 
                    } 
                %>
            </tbody>
        </table>
        <div class="total-count">
            <h3>共 <%= facilities.size() %> 筆設施資料</h3>
        </div>
    </div>
</body>
</html>
