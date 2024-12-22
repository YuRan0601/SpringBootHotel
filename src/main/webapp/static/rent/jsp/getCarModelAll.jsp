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
	href="/CloudSerenityHotel/static/rent/css/getCarModelAll.css">
<title>雲澄飯店租車系統</title>
</head>

<body>
	<nav id="sidebar">

		<img src="/CloudSerenityHotel/static/rent/cloud.png" alt="Logo"
			class="sidebar-img">

		<div class="scrollbar">
			<ul>
				<li><a href="/CloudSerenityHotel/static/rent/html/home.html"><i
						class="fa-solid fa-house"></i><span>首頁</span></a></li>
				<li><a href="/CloudSerenityHotel/rent/car-model/query-all"><i
						class="fa-solid fa-car-side"></i><span>車型管理</span></a></li>
				<li><i class="fa-solid fa-car"></i><span>車輛管理</span></li>
				<li><i class="fa-solid fa-box-archive"></i><span>訂單紀錄</span></li>
			</ul>
		</div>
	</nav>

	<article>
		<div class="search-box">
			<input type="text" placeholder="搜尋..." id="searchInput">
			<button id="searchButton">
				<i class="fa-solid fa-magnifying-glass"></i>
			</button>
			<button id="addCarButton" class="add-car-btn" onclick="insertCar()">新增車型</button>
		</div>

		<div class="grid-container">


		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
			<c:forEach var="car" items="${cars}">
				<div class="grid-item-wrapper">
					<div class="grid-item" onclick="getOneCar('${car.carId}')">

						<img src="data:image/png;base64,${car.image}" alt="描述圖片"
							class="image">

						<h3>${car.carModel}</h3>
						<p>${car.brand}</p>
					</div>
				</div>
			</c:forEach>
		</div>

	</article>


	<script>
		function getOneCar(carId) {
			window.location.href = "/CloudSerenityHotel/rent/car-model/get-one?carId="
					+ carId;
		}

		function insertCar(carId) {

			window.location.href = "/CloudSerenityHotel/static/rent/html/insertCarModel.html";
		}

		function selectBrand() {

			var brand = document.getElementById('searchInput').value;

			if (brand) {

				window.location.href = "/CloudSerenityHotel/rent/car-model/select-brand?brand="
						+ encodeURIComponent(brand);
			} else {
				alert('請輸入品牌名稱');
			}
		}

		document.getElementById('searchButton').addEventListener('click',
				selectBrand);
	</script>
</body>

</html>