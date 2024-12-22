<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css"
	integrity="sha512-5Hs3dF2AEPkpNAR7UiOHba+lRSJNeM2ECkwxUIxC1Q/FLycGTbNapWXB4tP889k5T5Ju8fs4b1P5z/iB4nMfSQ=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />

<link rel="stylesheet" href="../css/insertCarModel.css">
<title>雲澄飯店租車系統</title>

</head>

<body>
	<form method="post" action="/CloudSerenityHotel/rent/car-model/insert">
		<nav id="sidebar">

			<img src="/CloudSerenityHotel/static/rent/cloud.png" alt="Logo"
				class="sidebar-img">

			<div class="scrollbar">
				<ul>
					<li><a href="/CloudSerenityHotel/static/rent/html/home.html"><i class="fa-solid fa-house"></i><span>首頁</span></a></li>
					<li><a href="/CloudSerenityHotel/rent/car-model/query-all"><i
							class="fa-solid fa-car-side"></i><span>車型管理</span></a></li>
					<li><i class="fa-solid fa-car"></i><span>車輛管理</span></li>
					<li><i class="fa-solid fa-box-archive"></i><span>訂單紀錄</span></li>
				</ul>
			</div>
		</nav>

		<article>
			<div class="form-container">
				<h2>車型新增表單</h2>

				<!-- 車型名稱 -->
				<div class="form-group">
					<label for="model-name">車型名稱:</label> <input type="text"
						id="model-name" name="model-name" required>
				</div>

				<!-- 品牌 -->
				<div class="form-group">
					<label for="brand">品牌:</label> <input type="text" id="brand"
						name="brand" required>
				</div>

				<!-- 油耗量 (數字輸入框) -->
				<div class="form-group">
					<label for="fuel-consumption">油耗量 (單位：km/L):</label> <input
						type="text" id="fuel-consumption" name="fuel-consumption" required>
				</div>

				<!-- 車輛類型 -->
				<div class="form-group">
					<label for="vehicle-type">車輛類型:</label> <select id="vehicle-type"
						name="vehicle-type" required>
						<option value="機車">機車</option>
						<option value="小型車">小型車</option>
						<option value="緊湊型車">緊湊型車</option>
						<option value="中型車">中型車</option>
						<option value="全尺寸車">全尺寸車</option>
						<option value="豪華車">豪華車</option>
						<option value="運動型車">運動型車</option>
						<option value="SUV">SUV（運動型多功能車）</option>
						<option value="廂型車">廂型車</option>
						<option value="皮卡">皮卡</option>
						<option value="電動車">電動車</option>
					</select>
				</div>

				<!-- 車輛尺寸 -->
				<div class="form-group">
					<label for="vehicle-size">車輛尺寸:</label> <select id="vehicle-size"
						name="vehicle-size" required>
						<option value="小型">小型</option>
						<option value="中型">中型</option>
						<option value="全尺寸">全尺寸</option>
						<option value="大型">大型</option>
						<option value="超大">超大</option>
					</select>
				</div>

				<!-- 乘車人數 -->
				<div class="form-group">
					<label for="passenger-count">乘車人數:</label> <input type="number"
						id="passenger-count" name="passenger-count" required>
				</div>

				<!-- 車輛描述 -->
				<div class="form-group">
					<label for="vehicle-description">車輛描述:</label>
					<textarea id="vehicle-description" name="vehicle-description"
						rows="4" required></textarea>
				</div>


				<button type="submit">提交表單</button>

			</div>

		</article>
	</form>
</body>

</html>