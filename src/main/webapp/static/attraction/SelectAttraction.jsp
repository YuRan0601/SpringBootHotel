<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>功能選單</title>
    <style>
        body {
            font-family: "Arial", sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            text-align: center;
            background-color: #ffffff;
            padding: 30px 50px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: #9c70e8; /* 淡紫色 */
            margin-bottom: 20px;
        }
        .button-group {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        .button-group button {
            background-color: #9c70e8; /* 淡紫色 */
            color: white;
            font-size: 1.2em;
            border: none;
            border-radius: 5px;
            padding: 10px 20px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .button-group button:hover {
            background-color: #8a63d1; /* 更深的紫色 */
        }
        .button-group button:active {
            background-color: #7b58ba; /* 點擊時更深的紫色 */
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>管理功能選單</h1>
        <div class="button-group">
            <button onclick="location.href='/CloudSerenityHotel/static/attraction/InsertAttraction.html'">新增資料</button><br>
            <button onclick="location.href='/CloudSerenityHotel/static/attraction/GetUpdateData.html'">修改資料</button><br>
            <button onclick="location.href='/CloudSerenityHotel/static/attraction/DeleteAttraction.html'">刪除資料</button><br>
            <button onclick="location.href='/CloudSerenityHotel/static/attraction/GetAttraction.html'">查詢單筆資料</button><br>
            <button onclick="location.href='/CloudSerenityHotel/GetAllAttractions'">查詢所有資料</button>
        </div>
    </div>
</body>
</html>
