<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>會員功能總管</title>
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
<h1>會員功能總管</h1>
    <div class="menu">
        <a href="#">個人資料</a>
        <a href="#">訂房查詢</a>
        <a href="#">訂單查詢</a>
        <a href="../user/logout">登出</a>
    </div>
    <hr>
    <div>
        <p>歡迎，<%= session.getAttribute("userName") %>！</p>
        <p>請選擇您需要的功能。</p>
    </div>
</body>
</html>