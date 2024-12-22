<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-tw">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>新增管理員帳號</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">
<style>
#passwordMsg {
	color: #c70000;
	font-weight: bold;
}

#errorMessage {
	padding: 10px 0;
	margin: 0;
	color: #c70000;
	font-weight: bold;
	visibility: hidden;
	text-align: center;
}
</style>
</head>
<body>
	<div align="center">
		<h2>新增管理員帳號</h2>
		<form method="post" action="/CloudSerenityHotel/user/addAdmin">
			<label for="email">電子信箱</label>
			 <input type="email" id="email" name="email" placeholder="請輸入電子信箱" required>
			  <p></p> 
			  <label for="password">密碼</label>
			   <input type="password" id="password" name="password" placeholder="請輸入密碼" minlength="8" maxlength="64"
				required>
				 <span id="passwordMsg" style="visibility: hidden;">
				<i class="fa-solid fa-circle-xmark"></i>
			</span> <p></p> 
			<label for="name">姓名</label> 
			<input type="text" id="name"
				name="name" placeholder="請輸入姓名" required>
				<p></p>
				<button type="submit">註冊</button>
		</form>
		<p id="errorMessage">
			<i class="fa-solid fa-circle-xmark"></i>
			<%
			String errorMessage = (String) request.getAttribute("errorMessage");
			if (errorMessage != null) {
				out.print(errorMessage);
			}
			%>
		</p>
	</div>
	<script src="https://code.jquery.com/jquery-3.7.1.js"
		integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
		crossorigin="anonymous"></script>
	<script>
		//處理錯誤訊息
		$(document).ready(function() {
			showErrorMsg(); // 在頁面加載時執行
		});

		function showErrorMsg() {
			let msg = $('#errorMessage').text().trim(); // 取得錯誤訊息
			if (msg.length > 0) { // 如果訊息不為空，顯示錯誤訊息
				$('#errorMessage').css('visibility', 'visible');
			} else { // 如果沒有訊息，隱藏錯誤訊息
				$('#errorMessage').css('visibility', 'hidden');
			}
		}
        //password
        $('#password').blur(function () {
            let content = $(this).val();
            let contentLength = $(this).val().length;
            let rule1 = new RegExp(/[a-z]/i);
            let rule2 = new RegExp(/[0-9]/);
            let rule3 = new RegExp(/[!@#$%^*]/);
            if (contentLength == 0) {
                $('#passwordMsg').html('<i class="fa-solid fa-circle-xmark"></i> 密碼不可為空白');
                $('#passwordMsg').css('visibility', 'visible');
            } else if (contentLength < 8) {
                $('#passwordMsg').html('<i class="fa-solid fa-circle-xmark"></i> 密碼至少8個字');
                $('#passwordMsg').css('visibility', 'visible');
            } else if (!rule1.test(content)) {
                $('#passwordMsg').html('<i class="fa-solid fa-circle-xmark"></i> 密碼必須包含英文');
                $('#passwordMsg').css('visibility', 'visible');
            } else if (!rule2.test(content)) {
                $('#passwordMsg').html('<i class="fa-solid fa-circle-xmark"></i> 密碼必須包含數字');
                $('#passwordMsg').css('visibility', 'visible');
            } else if (!rule3.test(content)) {
                $('#passwordMsg').html('<i class="fa-solid fa-circle-xmark"></i> 密碼必須包含特殊字元!@#$%^*');
                $('#passwordMsg').css('visibility', 'visible');
            } else {
                $('#passwordMsg').css('visibility', 'hidden');
            }
        })
	</script>
</body>
</html>