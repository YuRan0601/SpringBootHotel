<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-tw">

<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>雲澄旅館-登入會員</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
		integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous" />
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css">
	<style>
		.login {
			margin: auto;
			padding: 50px 0;
			display: flex;
			justify-content: center;
		}

		.loginform {
			padding: 35px 35px;
			width: 400px;
			display: block;
			border-radius: 50px;
			background-color: skyblue;
		}

		.nav-bgc {
			background-color: lightblue;
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
	<nav class="navbar navbar-light nav-bgc">
		<div class="container-fluid">
			<a class="navbar-brand" href="#">雲澄旅館</a>
		</div>
	</nav>
	<div class="login container">
		<form class="loginform" method="post" action="/CloudSerenityHotel/user/login">
			<h2 style='text-align: center; margin: 0;'>歡迎登入</h2>
			<br>
			<label for="email" class="form-label fs-5">電子信箱</label>
			<input type="email" class="form-control" id="email" name="email" placeholder="請輸入電子信箱" required />
			<br>
			<label for="password" class="form-label fs-5">密碼</label>
			<input type="password" class="form-control" id="password" name="password" placeholder="請輸入密碼" minlength="8"
				maxlength="64" required />
			<br>
			<div class="d-grid gap-2 mx-auto">
				<button type="submit" class="btn btn-primary">登入</button>
			</div>
			<p id="errorMessage">
				<i class="fa-solid fa-circle-xmark"></i>
				<%
				String errorMessage = (String) request.getAttribute("errorMessage");
				if (errorMessage != null) {
					out.print(errorMessage);
				}
				%>
			</p>
			<p style='text-align: center; margin: 0;'>
				沒有帳號? <a href="register.jsp">立即註冊</a>
			</p>
		</form>
	</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous">

	</script>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
		crossorigin="anonymous"></script>
	<script>
		$(document).ready(function () {
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
	</script>
</body>

</html>