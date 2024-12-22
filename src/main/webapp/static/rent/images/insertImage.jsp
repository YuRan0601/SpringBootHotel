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
				<li><a href="/CloudSerenityHotel/static/rent/html/home.html"><i class="fa-solid fa-house"></i><span>首頁</span></a></li>
				<li><a href="/CloudSerenityHotel/rent/car-model/query-all"><i
						class="fa-solid fa-car-side"></i><span>車型管理</span></a></li>
				<li><i class="fa-solid fa-car"></i><span>車輛管理</span></li>
				<li><i class="fa-solid fa-box-archive"></i><span>訂單紀錄</span></li>
			</ul>
		</div>
	</nav>
 <script>
        document.getElementById('imageUpload').addEventListener('change', function (event) {
            const files = event.target.files;
            const previewContainer = document.getElementById('imagePreview');
            previewContainer.innerHTML = '';  // 清空現有的預覽

            // 循環處理每個上傳的檔案
            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                const reader = new FileReader();

                reader.onload = function (e) {
                    const img = document.createElement('img');
                    img.src = e.target.result;

                    const div = document.createElement('div');
                    div.classList.add('image-preview');

                    // 為每張圖片添加勾選框（checkbox）
                    div.innerHTML = `
                        <input type="checkbox" name="selectedImages" id="selectedImage${i}" />
                        <label for="selectedImage${i}">選擇此圖</label>
                    `;
                    div.appendChild(img);

                    // 添加到預覽區域
                    previewContainer.appendChild(div);
                };

                // 讀取圖片檔案
                reader.readAsDataURL(file);
            }
        });
    </script>

</body>

</html>