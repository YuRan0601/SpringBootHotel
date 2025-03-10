<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>刪除結果</title>
    <style>
        /* 核心樣式 */
        .gtco-video .overlay {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.5);
            -webkit-transition: 0.5s;
            -o-transition: 0.5s;
            transition: 0.5s;
        }
        .gtco-video:hover .overlay {
            background: rgba(0, 0, 0, 0.7);
        }
        .gtco-cover > .gtco-container {
            position: relative;
            z-index: 10;
        }
        .fh5co-card-item figure .overlay {
            opacity: 0;
            visibility: hidden;
            z-index: 10;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            position: absolute;
            background: rgba(0, 0, 0, 0.4);
            -webkit-transition: 0.7s;
            -o-transition: 0.7s;
            transition: 0.7s;
        }
        .fh5co-card-item figure .overlay i {
            z-index: 12;
            color: #fff;
            font-size: 30px;
            position: absolute;
            margin-left: -15px;
            margin-top: -45px;
            top: 50%;
            left: 50%;
            -webkit-transition: 0.3s;
            -o-transition: 0.3s;
            transition: 0.3s;
        }
        .fh5co-card-item:hover figure .overlay, .fh5co-card-item:focus figure .overlay {
            opacity: 1;
            visibility: visible;
        }
        .fh5co-card-item:hover figure .overlay i, .fh5co-card-item:focus figure .overlay i {
            margin-top: -15px;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 20px;
        }
        h2 {
            color: #9c70e8; /* 淡紫色 */
            font-size: 36px;
            text-align: center;
            margin-top: 50px;
        }
        .container {
            width: 50%;
            margin: auto;
            background-color: #ffffff;
            padding: 30px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
        }
        label {
            font-size: 16px;
            color: #555;
        }
        input[type="text"] {
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
            background-color: #f2f2f2;
        }
        input[type="submit"] {
            width: 100%;
            padding: 12px;
            background-color: #9c70e8; /* 淡紫色 */
            border: none;
            border-radius: 5px;
            color: white;
            font-size: 18px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        input[type="submit"]:hover {
            background-color: #8360c3; /* 深一點的淡紫色 */
        }
        .form-group {
            margin-bottom: 20px;
        }

        /* Navigation styles */
        nav {
            background: #333;
            color: #fff;
        }
        .gtco-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 20px;
        }
        .gtco-logo a {
            text-decoration: none;
            color: white;
            font-size: 1.5em;
        }
        .menu-1 ul {
            list-style: none;
            display: flex;
            margin: 0;
            padding: 0;
        }
        .menu-1 ul li {
            margin: 0 15px;
            position: relative;
        }
        .menu-1 ul li a {
            color: white;
            text-decoration: none;
        }
        .has-dropdown:hover .dropdown {
            display: block;
        }
        .dropdown {
            display: none;
            position: absolute;
            top: 100%;
            left: 0;
            background: #fff;
            color: #333;
            list-style: none;
            margin: 0;
            padding: 10px 0;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .dropdown li {
            padding: 5px 20px;
        }
        .dropdown li a {
            color: #333;
            text-decoration: none;
        }
        .dropdown li a:hover {
            color: #9c70e8;
        }
        header {
            background-size: cover;
            height: 300px;
            color: black;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
        }
    </style>
</head>
<body>
    <nav>
        <div class="gtco-container">
            <div class="gtco-logo"><a href="#">雲澄旅館</a></div>
            <div class="menu-1">
                <ul>
                    <li><a href="../static/facility/SelectFacility.jsp">返回首頁</a></li>
                    <li><a href="../static/facility/GetUpdateDataFacility.html">新增資料</a></li>
                    <li><a href="../static/facility/GetUpdateDataFacility.html">修改資料</a></li>
                    <li><a href="../static/facility/DeleteFacility.html">刪除資料</a></li>
                    <li><a href="../static/facility/GetFacility.html">查詢單筆資料</a></li>
                    <li><a href="GetAllFacilities">查詢所有資料</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <header id="gtco-header" class="gtco-cover gtco-cover-sm" role="banner" style="background-image: url(../images/Background1.jpeg)">
        <div class="overlay"></div>
        <div class="gtco-container">
            <div class="row">
                <div class="col-md-12 col-md-offset-0 text-center">
                    <div class="row row-mt-15em">
                        <div class="col-md-12 mt-text animate-box" data-animate-effect="fadeInUp">
                            <h1>雲澄<br>CloudSerenity Hotel</h1>    
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </header>
    
    <div class="container">
        <h2>刪除結果</h2>
        <p>${message}</p>
	<div style="text-align: center; margin-top: 20px;">
	    <a href="/CloudSerenityHotel/static/facility/DeleteFacility.html" style="font-size: 18px; color: #9c70e8;">返回</a>
	</div>
    </div>
</body>
</html>
