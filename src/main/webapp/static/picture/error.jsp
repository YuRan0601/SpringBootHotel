<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<html>
<head><title>錯誤</title></head>
<body style="background-color:#fdf5e6">
<div align="center">
    <h2><%= request.getAttribute("message") %></h2>
    <a href="AttractionsList.jsp">返回景點列表</a>
</div>
</body>
</html>
