<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>商品上架</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
	<link rel="stylesheet" href="/CloudSerenityHotel/static/product/styles/ProductLaunched2.css">
</head>
<body>
<form method="post" action="/CloudSerenityHotel/product/update" enctype="multipart/form-data">
    <div Class="top">
        <h2>修改商品</h2>
        <div>
            <a href="/CloudSerenityHotel/ProductGetAllController" style="text-decoration: none;color:inherit;"><button
                    Class="breakButton" type="button">返回</button></a>
            <input Class="insertButton" type="submit" value="確認送出">
        </div>
    </div>
		<input type="hidden" name="product_id" readonly value="${products.product_id}">
        <table class="table">
            <tr>
                <th class="formTitle">商品名稱<span class="required">(必填)</span></th>
            </tr>
            <tr>
                <td class="text"><input type="text" name="name" value="${products.name}"></td>
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
                    <input type="text" name="categoriesName" list="classiFication" value="${products.categories[0].name}" >
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
                <td class="text"><input type="text" name="price" value="${products.price.toBigInteger()}"></td>
            </tr>

            <tr>
                <th class="formTitle">特價</th>
            </tr>
            <tr>
                <td class="text"><input type="text" name="special_price" value="${products.special_price.toBigInteger()}"></td>
            </tr>

            <tr>
                <th class="formTitle">商品描述</th>
            </tr>
            <tr>
                <td class="text"><textarea name="description">${products.description}</textarea></td>
            </tr>

        </table>
    </form>
    
            <script src="/CloudSerenityHotel/static/product/js/ProductLaunched.js"></script>


</body>

</html>