<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>查詢結果</title>
</head>
<body>
<div align="center">
<h2>管 理 員 資 料</h2>
<table border="1">
<tr style="background-color:#a8fefa">
<th>使用者編號<th>使用者姓名<th>電子信箱<th>密碼<th>使用狀態<th>身分組<th>帳號更新時間<th colspan='2'></tr>
	<c:forEach items="${userData}" var="user" varStatus="s" >
  		<tr><td>${user.userId}
		<td>${user.userName}
		<td>${user.email}
		<td>${user.password}
		<td>${user.userStatus}
		<td>${user.userIdentity}
		<td>${user.accountUpdateTime}
		<c:choose>
		<c:when test="${user.userStatus == 'In_use'}">
		<c:set var="actionStr" value="註銷帳號"/>
		<c:set var="action" value="delacc"/>
		</c:when>
		<c:otherwise>
		<c:set var="actionStr" value="恢復帳號"/>
		<c:set var="action" value="recover"/>
		</c:otherwise>
		</c:choose>
		<td>
		<form action='/CloudSerenityHotel/user/getUpdateData' method='post'>
      	<input type='hidden' name='userId' value='${user.userId}'>
      	<input type='hidden' name='identity' value='admin'>
      	<input type='submit' value='修改資料'>
      	</form>
		<td>
		<form action='/CloudSerenityHotel/user/statusLock' method='post'>
		<input type='hidden' name='userId' value='${user.userId}'>
		<input type='hidden' name='action' value='${action}'>
		<input type='hidden' name='identity' value='admin'>
      	<input type='submit' value='${actionStr}'>
		</form>   
		</tr>
		<c:set var="num" value="${s.count}"/>
	</c:forEach>
</table>
<h3>共${num}筆 管理員資料</h3>
<a href="/CloudSerenityHotel/static/user/protected/queryAdminData.jsp">回查詢頁面</a>
</div>
</body>
</html>