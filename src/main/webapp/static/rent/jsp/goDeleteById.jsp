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

<link rel="stylesheet" type="text/css"
	href="/CloudSerenityHotel/static/rent/css/insertCarModel.css">
<title>雲澄飯店租車系統</title>
</head>
<body>
	<form action="/CloudSerenityHotel/rent/car-model/delete" method="post"
		id="DeleteCarModel-form">

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
				<h2>刪除車型</h2>

				<!-- 車型名稱 -->
				<div class="form-group">
					<label for="carModel">車型編號:</label> <input type="text" disabled
						name="carId" value="${carId}"> <input type="hidden"
						name="carId" value="${carId}" />

				</div>

				<div class="form-group">
					<label for="carModel">車型名稱:</label> <input type="text" disabled
						name="carId" value="${carModel}">
				</div>

				<!-- 品牌 -->
				<div class="form-group">
					<label for="brand">品牌:</label> <input type="text" disabled
						name="brand" value="${brand}">
				</div>

				<!-- 油耗量 (數字輸入框) -->
				<div class="form-group">
					<label for="fuelEfficiency">油耗量 (單位：km/L):</label> <input
						type="text" disabled name="fuelEfficiency"
						value="${fuelEfficiency}">
				</div>

				<!-- 車輛類型 -->
				<div class="form-group">
					<label for="carType">車輛類型:</label> <input type="text" disabled
						name="carType" value="${carType}">
				</div>

				<div class="form-group">
					<label for="carSize">車輛尺寸:</label> <input type="text" disabled
						name="carSize" value="${carSize}">

				</div>

				<!-- 乘車人數 -->
				<div class="form-group">
					<label for="seatingCapacity">乘車人數:</label> <input type="text"
						disabled name="seatingCapacity" value="${seatingCapacity}">

				</div>

				<div class="form-group">
					<label for="totalVehicles">車輛總數:</label> <input type="text"
						disabled name="totalVehicles" value="${totalVehicles}">
				</div>

				<div class="form-group">
					<label for="availableVehicles">可用數量:</label> 
					<input type="text"
						disabled name="availableVehicles" value="${availableVehicles}">

				</div>

				<!-- 車輛描述 -->
				<div class="form-group">
					<label for="description">車輛描述:</label><input type="text"
						disabled name="description" value="${description}">
				</div>


				<button type="submit">刪除</button>

			</div>

		</article>
	</form>
</html>