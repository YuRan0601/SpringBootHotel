<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新增景點</title>
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
        form {
            text-align: left;
            margin-top: 20px;
        }
        label {
            font-size: 1.1em;
            color: #9c70e8; /* 淡紫色 */
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
        }
        input[type="submit"] {
            background-color: #9c70e8; /* 淡紫色 */
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 1.1em;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #8a63d1; /* 更深的紫色 */
        }
    </style>
</head>
<body>
    <h1>新增景點</h1>
    <form id="createAttractionForm" action="#" method="POST">
        <label for="name">名稱:</label>
        <input type="text" id="name" name="name" required><br><br>

        <label for="description">描述:</label>
        <textarea id="description" name="description" required></textarea><br><br>

        <label for="location">位置:</label>
        <input type="text" id="location" name="location" required><br><br>

        <label for="openingHours">開放時間:</label>
        <input type="text" id="openingHours" name="openingHours" required><br><br>

        <label for="contactInfo">聯絡資訊:</label>
        <input type="text" id="contactInfo" name="contactInfo" required><br><br>

        <label for="typeId">類型ID:</label>
        <input type="number" id="typeId" name="typeId" required><br><br>

        <label for="imageUrl">圖片URL:</label>
        <input type="text" id="imageUrl" name="imageUrl" required><br><br>

        <button type="submit">新增</button>
    </form>

    <script>
    document.getElementById('createAttractionForm').addEventListener('submit', function(e) {
        e.preventDefault(); // 防止頁面重新加載

        const formData = {
            name: document.getElementById('name').value,
            description: document.getElementById('description').value,
            location: document.getElementById('location').value,
            openin
