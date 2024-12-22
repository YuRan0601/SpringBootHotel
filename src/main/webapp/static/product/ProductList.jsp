<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 這個標籤記得要加，forEach跟if標籤會沒辦法使用，然後不知道為什麼就會一直觸發很多的toString錯誤 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>商品後台管理</title>
 	<link rel="stylesheet" href="/CloudSerenityHotel/static/product/styles/ProductList2.css">
</head>

<body>
    <div Class="top">
        <h2>所有商品</h2>
        <a href="/CloudSerenityHotel/static/product/ProductLaunched2.html" style="text-decoration: none;color:inherit;"><button
                Class="insertButton">新增商品</button></a>
    </div>
    
    <form method="post" action="/CloudSerenityHotel/product/select">
        <div Class="selectText">
            <input type="text" name="product_id" placeholder="請輸入商品編號">
            <input type="submit" value="搜尋" Class="selectButton" />
            <!-- <button Class="selectButton default">搜尋</button> -->
        </div>
    </form>
    
        <table>
            <thead>
                <tr class="titleField">
                    <th class="title">編號</th>
                    <th class="title">圖片</th>
                    <th class="title">商品名稱</th>
                    <th class="title">原價格</th>
                    <th class="title">特價</th>
                    <th class="title">上架狀態</th>
                    <th class="title">操作</th>
                </tr>
            </thead>

            <tbody>

                <tr>
                    <td>${products.product_id}</td>
                    
                    <td>
                    	<!-- 判斷如果是主圖的話，就顯示這張主圖 -->
                    	<c:forEach items="${products.productImages}" var="image">
                    		<c:if test="${image.is_primary}">
                    			<img src="${image.image_url}">
                    		</c:if>
                    	</c:forEach>
                    </td>
                    <td>${products.name}</td>
                    <td>${products.price.toBigInteger()}</td>
                    <td>${products.special_price.toBigInteger()}</td>
                    <td><button type="button" class="productLaunched">上架</button></td>
                        
                    <td>
                     <form method="post" action="/CloudSerenityHotel/product/GetupdateData" style="display: inline;">
                    <input type="hidden" name="product_id" value="${products.product_id}">
                    <button class="default">編輯</button>
                    </form>
                    
                    <form method="post" action="/CloudSerenityHotel/product/delete" style="display: inline;">
                    <input type="hidden" name="product_id" value="${products.product_id}">
                    <button type="submit" class="delect">刪除</button>
                    </form>
                    
                    </td>
                </tr>
                
            </tbody>
        </table>
          <script src="/CloudSerenityHotel/static/product/js/ProductList.js"></script>
</body>

</html>