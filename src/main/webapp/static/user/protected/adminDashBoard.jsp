<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>管理員後台 - 功能總管</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .menu {
            margin: 10px 0;
        }
        .menu a {
            text-decoration: none;
            margin-right: 15px;
            color: #007BFF;
        }
        .menu a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<h1>管理員後台</h1>
<h3><%= session.getAttribute("userName") %> 管理員</h3>
    <div class="menu">
        <a href="#">管理功能1</a>
        <a href="#">管理功能2</a>
        <a href="#">管理功能3</a>
        <a href="#">管理功能4</a>
        <a href="#">管理功能5</a>
        <a href="../user/logout">登出</a>
    </div>
    <hr>
    <div>
        <p>請選擇您需要的管理功能。</p>
    </div>
</body>
</html>