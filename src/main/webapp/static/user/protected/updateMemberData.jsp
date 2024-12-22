<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改會員資料</title>
</head>
<body>
<div align="center">
<h2>修改會員資料</h2>
<form method="post" action="/CloudSerenityHotel/user/updateData">
			<label for="userId">使用者編號</label>
			 <input type="text" id="userId" name="userId" value="${userData.userId}" readonly>
			  <p></p> 			
			<label for="name">使用者名稱</label>
			 <input type="text" id="name" name="name" value="${userData.userName}"  required>
			  <p></p> 			
			  <label for="email">電子信箱</label>
			 <input type="email" id="email" name="email" value="${userData.email}" required>
			  <p></p> 			
			  <label for="password">密碼</label>
			 <input type="text" id="password" name="password" value="${userData.password}" required>
			  <p></p> 
			  <label for="accUpdateTime">帳號更新時間</label>
			 <input type="text" id="accUpdateTime" name="accUpdateTime" value="${userData.accountUpdateTime}" readonly>
			  <p></p> 
			  <label for="userStatus">使用狀態</label>
			 <input type="text" id="userStatus" name="userStatus" value="${userData.userStatus}" readonly>
			  <p></p> 
			  <label for="gender">性別</label>
			 <input type="text" id="gender" name="gender" value="${memberData.gender}" required>
			  <p></p> 
			  <label for="birthday">生日</label>
			 <input type="date" id="birthday" name="birthday" value="${memberData.birthday}" required>
			  <p></p> 
			  <label for="phone">電話</label>
			 <input type="tel" id="phone" name="phone" value="${memberData.phone}" required>
			  <p></p> 
			  <label for="personalIdNo">身分證字號</label>
			 <input type="text" id="personalIdNo" name="personalIdNo" value="${memberData.personalIdNo}">
			  <p></p> 
			  <label for="country">國家</label>
			 <input type="text" id="country" name="country" value="${memberData.country}" required>
			  <p></p> 
			  <label for="address">地址</label>
			 <input type="text" id="address" name="address" value="${memberData.address}" required>
			  <p></p> 
			  <label for="passportNo">護照號碼</label>
			 <input type="text" id="passportNo" name="passportNo" value="${memberData.passportNo}">
			  <p></p> 
			  <label for="registerDate">註冊時間</label>
			 <input type="text" id="registerDate" name="registerDate" value="${memberData.registerDate}" readonly>
			  <p></p> 
			  <label for="dataUpdateTime">資料更新時間</label>
			 <input type="text" id="dataUpdateTime" name="dataUpdateTime" value="${memberData.dataUpdateTime}" readonly>
			  <p></p>
			  <input type="hidden" id="identity" name="identity" value="user" readonly> 
  			 <button type="submit">修改</button>
</form>
</body>
</html>