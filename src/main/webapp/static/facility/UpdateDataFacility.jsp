<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>更新設施資料</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 20px;
        }
        h2 {
            color: #9c70e8; /* 淡紫色 */
            font-size: 36px;
            text-align: center;
            margin-top: 50px;
        }
        .container {
            width: 50%;
            margin: auto;
            background-color: #ffffff;
            padding: 30px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
        }
        label {
            font-size: 16px;
            color: #555;
        }
        input[type="text"] {
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
            background-color: #f2f2f2;
        }
        input[type="submit"] {
            width: 100%;
            padding: 12px;
            background-color: #9c70e8; /* 淡紫色 */
            border: none;
            border-radius: 5px;
            color: white;
            font-size: 18px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        input[type="submit"]:hover {
            background-color: #8360c3; /* 深一點的淡紫色 */
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group input[type="text"] {
            width: 100%;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>更新設施資料</h2>
        <form action="UpdateFacility" method="post">
            <div class="form-group">
                <label for="facilityId">設施ID:</label>
                <input type="text" name="facilityId" value="${facility.facilityId != null ? facility.facilityId : ''}" readonly />
            </div>
            <div class="form-group">
                <label for="name">設施名稱：</label>
                <input type="text" name="name" value="${facility.name}" required />
            </div>
            <div class="form-group">
                <label for="description">簡介：</label>
                <input type="text" name="description" value="${facility.description}" required />
            </div>
            <div class="form-group">
                <label for="location">位置：</label>
                <input type="text" name="location" value="${facility.location}" required />
            </div>
            <div class="form-group">
                <label for="availabilityHours">開放時間：</label>
                <input type="text" name="availabilityHours" value="${facility.availabilityHours}" required />
            </div>
            <div class="form-group">
                <input type="submit" value="更新設施資料" />
            </div>
        </form>
    </div>
</body>
</html>
