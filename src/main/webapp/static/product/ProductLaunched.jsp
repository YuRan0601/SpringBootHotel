<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>商品上架</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
	<link rel="stylesheet" href="../product/styles/ProductLaunched.css">
</head>
<body>
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
    
            <script src="/CloudSerenityHotel/static/product/js/ProductLaunched2.js"></script>


</body>

</html>