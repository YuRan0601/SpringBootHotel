<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-tw">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>查詢會員資料</title>
</head>
<body>
<div align="center">
<h2>查詢會員資料</h2>
<form method="post" action="/CloudSerenityHotel/admin/queryAllData">
	<input type="text" name="targetIdentity" value="user" readonly hidden />
	<button type="submit">查詢全部資料</button>
</form>
<form method="post" action="/CloudSerenityHotel/admin/queryMemberById">
	<p>以使用者編號查詢</p>
	<input type="number" min="1" name="id" placeholder="請輸入使用者編號" required />
	<button type="submit">查詢</button>
</form>
<form method="post" action="/CloudSerenityHotel/admin/queryMemberByName">
	<p>以使用者名稱查詢</p>
	<input type="text" name="name" placeholder="請輸入使用者名稱" required />
	<button type="submit">查詢</button>
</form>
</div>
</body>
</html>