<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>商品上架</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/product/styles/ProductLaunched.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/common/aside.css" />
	
	 	<!-- 引入 jQuery -->
	<script
      src="https://code.jquery.com/jquery-3.7.1.js"
      integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
      crossorigin="anonymous"
    ></script>

	<!-- 引入 Bootstrap CSS -->
	<link
  	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
  	rel="stylesheet"
	/>

	<!-- 引入 Bootstrap Icons -->
	<link
  	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
  	rel="stylesheet"
	/>

	<!-- 引入 Bootstrap JS -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	</head>
</head>
<body>
<aside class="aside"></aside>
 <div class="content">

<form method="post" action="/CloudSerenityHotel/product/insert" enctype="multipart/form-data">
    <div Class="top">
        <h2>新增商品</h2>
        <div>
            <a href="/CloudSerenityHotel/ProductGetAllController" style="text-decoration: none;color:inherit;"><button
                    Class="breakButton" type="button">返回</button></a>
            <input Class="insertButton" type="submit" value="確認送出">
            <!-- <button Class="insertButton">確認送出</button> -->
        </div>
    </div>

        <table class="table">
            <tr>
                <th class="formTitle">商品名稱<span class="required">(必填)</span></th>
            </tr>
            <tr>
                <td class="text"><input type="text" name="name"></td>
            </tr>

            <tr>
                <th class="formTitle">商品圖片<span class="required">(必填)</span></th>
            </tr>
            <tr>
                <td class="productImg">
                    <input id="upload" type="file" name="file[]" accept="image/*" style="display: none;" multiple>
                    <label id="uploadImageText" type="button" for="upload">上傳圖片</label>
                </td>
            </tr>

            <tr>
                <th class="formTitle">分類</th>
            </tr>
                <tr>
                <td class="text">
                 <input type="text" name="categoriesName" list="classiFication" value="${categories.name}" >
                    <datalist id="classiFication">
                        <option>新品</option>
                        <option>熱銷商品</option>
                        <option>禮盒</option>
                    </datalist>
                </td>
            </tr>

            <tr>
                <th class="formTitle">售價<span class="required">(必填)</span></th>
            </tr>
            <tr>
                <td class="text"><input type="text" name="price"></td>
            </tr>

            <tr>
                <th class="formTitle">特價</th>
            </tr>
            <tr>
                <td class="text"><input type="text" name="special_price"></td>
            </tr>

            <tr>
                <th class="formTitle">商品描述</th>
            </tr>
            <tr>
                <td class="text"><textarea name="description"></textarea></td>
            </tr>

        </table>
    </form>
    
            <script src="${pageContext.request.contextPath}/static/product/js/ProductLaunched2.js"></script>

</div>
</body>

</html>