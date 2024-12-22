<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
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
	href="/CloudSerenityHotel/static/rent/images/image.css">
<title>雲澄飯店租車系統</title>

</head>

<body>

	<nav id="sidebar">
		<img src="/CloudSerenityHotel/static/rent/cloud.png" alt="Logo"
			class="sidebar-img">
		<div class="scrollbar">
			<ul>
				<li><i class="fa-solid fa-house"></i><span>首頁</span></li>
				<li><i class="fa-solid fa-car-side"></i><span>車型管理</span></li>
				<li><i class="fa-solid fa-car"></i><span>車輛管理</span></li>
				<li><i class="fa-solid fa-box-archive"></i><span>訂單紀錄</span></li>
			</ul>
		</div>
	</nav>

	<form action="" method="post" enctype="multipart/form-data"></form>
	<article>

		<div class="upload-container">
			<h2>圖片管理</h2>

			<!-- 隱藏原來的 input，並使用 label 作為自定義按鈕 -->
			<input type="file" id="imageUpload" name="images" multiple
				accept="image/*" style="display: none;">

			<!-- 自定義按鈕 -->
			<label for="imageUpload" class="upload-btn">上傳圖片</label>

			<!-- 圖片預覽區域 -->
			<div class="image-preview-container" id="imagePreview"></div>

			<div id="imageUrls"></div>
		</div>
		<div class="button-container">

			<i class="fa-solid fa-crown btn crown-btn" value="主圖"
				onclick="imageUpdate(${carId})"></i> <i
				class="fa-solid fa-plus btn crown-btn" value="新增"
				onclick="imageInsert(${carId})"></i> <i
				class="fa-solid fa-trash btn crown-btn" value="刪除"
				onclick="imageDelete(${carId})"></i>

		</div>
	</article>


	<script>
	 function imageUpdate(carId) {
        
         window.location.href = "/CloudSerenityHotel/rent/car-model/go-update-by-id?carId=" + carId;
     }

     function imageInsert(carId) {
    	 const idInput = document.createElement('input');
         idInput.value = carId;
         idInput.name = 'carId';
         form.appendChild(idInput);
         
    	 document.body.appendChild(form);
    	 form.submit();  // 提交表單
         <!-- window.location.href = "/CloudSerenityHotel/rent/car-model/insert-image?carId=" + carId; -->
     }

     function imageDelete(carId) {
        
         window.location.href = "/CloudSerenityHotel/rent/car-model/go-delete-image?carId=" + carId;
     }
			
      document.getElementById('imageUpload').addEventListener('change', function (event) {
            const files = event.target.files;
            const previewContainer = document.getElementById('imagePreview');
            previewContainer.innerHTML = '';  // 清空現有的預覽
			
            form = document.createElement('form');
            form.method="post"; 
            form.action="/CloudSerenityHotel/rent/car-model/insert-image";
            form.id ="insertImages";
            
            // 循環處理每個上傳的檔案
            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                const reader = new FileReader();
			
                reader.onload = function (e) {
                    const img = document.createElement('img');
                    img.src = e.target.result;

                    const div = document.createElement('div');
                    div.classList.add('image-preview');
					
                    input = document.createElement('input');
                    input.name = 'images' + (i + 1);
                    input.value = e.target.result;
                    // 為每張圖片添加勾選框（checkbox）
                    div.innerHTML = `
                        <input type="checkbox" name="selectedImages" id="selectedImage${i}" />
                        <label for="selectedImage${i}">選擇此圖</label>
                    `;
                    div.appendChild(img);

                    // 添加到預覽區域
                    previewContainer.appendChild(div);
                    
                    form.appendChild(input);
                };

                // 讀取圖片檔案
                reader.readAsDataURL(file);
                
                
            }
        });
        
     </script>

</body>

</html>