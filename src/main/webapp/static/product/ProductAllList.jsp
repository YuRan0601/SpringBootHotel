<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>商品後台管理</title>
 	<link rel="stylesheet" href="${pageContext.request.contextPath}/static/product/styles/ProductList2.css">
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

<body>
<aside class="aside"></aside>
 <div class="content">
 
    <div Class="top">
        <h2>所有商品</h2>
        <a href="${pageContext.request.contextPath}/static/product/ProductLaunched2.html" style="text-decoration: none;color:inherit;"><button
                Class="insertButton">新增商品</button></a>
    </div>


<form method="get" action="select">
        <div Class="selectText">
            <input type="text" name="productId" placeholder="請輸入商品編號">
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

                <c:forEach items="${allProducts}" var="products">
                <tr>
                    <td>${products.productId}</td>
                    <td>
                    	<!-- 判斷如果是主圖的話，就顯示這張主圖 -->
                    	<c:forEach items="${products.productImages}" var="image">
                    		<c:if test="${image.isprimary}">
                    			<img src="${image.imageUrl}">
                    		</c:if>
                    	</c:forEach>
                    </td>
                    <td>${products.name}</td>
                    <td>${products.price.toBigInteger()}</td>
                    <td>${products.specialPrice.toBigInteger()}</td>
					<td><button type="button" class="productLaunched">上架</button></td>
					
                    <td>
                     <form method="post" action="getUpdate/${products.productId}" style="display: inline;">
                    <input type="hidden" name="productId">
                    <button class="default">編輯</button>
                    </form>
                    
                    <form method="post" action="delete/${products.productId}" style="display: inline;">
                    <input type="hidden" name="productId">
                    <button type="submit" class="delect">刪除</button>
                    </form>
                    </td>
                </tr>
                </c:forEach>

                
            </tbody>
        </table>
        
<script src="${pageContext.request.contextPath}/static/product/js/ProductList.js">
</script>
 
 
 </div>
</body>

</html>