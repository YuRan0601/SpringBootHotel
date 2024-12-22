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

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.0.0-beta.2.4/assets/owl.carousel.min.css"></link>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.0.0-beta.2.4/assets/owl.theme.default.min.css"></link>
<script
	src='https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js'></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.0.0-beta.2.4/owl.carousel.min.js"></script>


<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
	rel="stylesheet">

<!-- owl.carousel -->
<link rel="stylesheet" href="owl.carousel.min.css">
<link rel="stylesheet" href="owl.theme.default.min.css">
<script src="jquery.min.js"></script>
<script src="owl.carousel.js"></script>

<!-- owl.carousel -->

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/assets/owl.carousel.min.css"></link>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/assets/owl.theme.default.min.css"></link>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/owl.carousel.min.js"></script>

<link rel="stylesheet" href="js/owl.carousel/owl.carousel.css">
<script src="js/owl.carousel/owl.carousel.js"></script>
<script src="js/owl.carousel/theme.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/assets/owl.carousel.min.css"></link>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/assets/owl.theme.default.min.css"></link>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/owl.carousel.min.js"></script>

<!-- owl.carousel 尾 -->

<link rel="stylesheet"
	href="/CloudSerenityHotel/static/rent/css/getCarOne.css">
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

		<div class="image-container">
			<div class="owl-carousel owl-theme">
				<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
				<c:forEach var="image" items="${images}">
					<li class="items"><img src="data:image/png;base64,${image}"
						alt="描述圖片" class="image"></li>
				</c:forEach>
			</div>
		</div>
		<h3>${carModel}</h3>

		<div class="car-description">
			<h4>車型介紹</h4>
			<p>${description}</p>

		</div>


		<div class="button-container">
			<i class="fa-solid fa-image image-btn" onclick="imagsAll(${carId})"></i>
			<i class="fa-solid fa-pen-to-square edit-btn"
				onclick="updateById(${carId})"></i> <i
				class="fa-solid fa-trash delete-btn" onclick="deleteById(${carId})"></i>
		</div>

		<div class="footer-info-wrapper">
			<div class="footer-info-block">
				<h4>編號</h4>
				<p>${carId}</p>
			</div>
			<div class="footer-info-block">
				<h4>品牌</h4>
				<p>${brand}</p>
			</div>
			<div class="footer-info-block">
				<h4>油耗</h4>
				<p>${fuelEfficiency}km/L</p>
			</div>
			<div class="footer-info-block">
				<h4>類型</h4>
				<p>${carType}</p>
			</div>
			<div class="footer-info-block">
				<h4>尺寸</h4>
				<p>${carSize}</p>
			</div>
			<div class="footer-info-block">
				<h4>人數</h4>
				<p>${seatingCapacity}</p>
			</div>
			<div class="footer-info-block">
				<h4>創建時間</h4>
				<p>${createdAt}</p>
			</div>
			<div class="footer-info-block">
				<h4>更新時間</h4>
				<p>${updatedAt}</p>
			</div>
		</div>

	</article>


	<script>
			function updateById(carId) {  
       		   window.location.href = "/CloudSerenityHotel/rent/car-model/go-update-by-id?carId=" + carId;
    		}
			
			function deleteById(carId) {
				 window.location.href = "/CloudSerenityHotel/rent/car-model/go-delete-by-id?carId=" + carId;
			}
			
			function imagsAll(carId) {
				 window.location.href = "/CloudSerenityHotel/rent/car-model/images-all?carId=" + carId;
			}
			
	</script>

	<!-- <script>
	  $(".owl-carousel").owlCarousel({
	        items: 1,  // 每次顯示一張圖片
	        //loop: true,  // 開啟循環播放
	        margin: 10,  // 圖片之間的間隔
	        responsive: {
	            0: {
	                items: 1  // 在小螢幕上顯示1張圖片
	            },
	            600: {
	                items: 1  // 在中等螢幕上顯示1張圖片
	            },
	            1000: {
	                items: 1  // 在大螢幕上顯示1張圖片
	            }
	        },
	        autoWidth: true, // 關閉自動寬度調整
	        onInitialized: function() {
	            console.log('Owl Carousel initialized');
	        }
		});
	</script> -->

</body>

</html>