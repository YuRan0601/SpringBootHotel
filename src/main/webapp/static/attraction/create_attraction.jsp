<!-- create_attraction.jsp -->
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新增景點</title>
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
		}
        body {
            margin: 0;
            font-family: Arial, sans-serif;
        }
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
        body {
            font-family: "Arial", sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 80%;
            margin: 50px auto;
            padding: 20px;
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            text-align: center;
        }
        h2 {
            font-size: 2em;
            color: #9c70e8; /* 淡紫色 */
            margin-bottom: 20px;
        }
        form {
            text-align: left;
            margin-top: 20px;
        }
        label {
            font-size: 1.1em;
            color: #9c70e8; /* 淡紫色 */
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
        }
        input[type="submit"] {
            background-color: #9c70e8; /* 淡紫色 */
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 1.1em;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #8a63d1; /* 更深的紫色 */
        }
    </style>
</head>
<body>
    <h1>新增景點</h1>
    <form id="createAttractionForm">
        <label for="name">名稱:</label>
        <input type="text" id="name" name="name" required><br><br>

        <label for="description">描述:</label>
        <textarea id="description" name="description" required></textarea><br><br>

        <label for="location">位置:</label>
        <input type="text" id="location" name="location" required><br><br>

        <label for="openingHours">開放時間:</label>
        <input type="text" id="openingHours" name="openingHours" required><br><br>

        <label for="contactInfo">聯絡資訊:</label>
        <input type="text" id="contactInfo" name="contactInfo" required><br><br>

        <label for="typeId">類型ID:</label>
        <input type="number" id="typeId" name="typeId" required><br><br>

        <label for="imageUrl">圖片URL:</label>
        <input type="text" id="imageUrl" name="imageUrl" required><br><br>

        <button type="submit">新增</button>
    </form>

    <script>
        document.getElementById('createAttractionForm').addEventListener('submit', function(e) {
            e.preventDefault();
            let formData = {
                name: document.getElementById('name').value,
                description: document.getElementById('description').value,
                location: document.getElementById('location').value,
                openingHours: document.getElementById('openingHours').value,
                contactInfo: document.getElementById('contactInfo').value,
                typeId: document.getElementById('typeId').value,
                imageUrl: document.getElementById('imageUrl').value
            };

            $.ajax({
                url: '/api/attractions/',  // AttractionController的POST請求URL
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function(data) {
                    alert('景點新增成功');
                    location.href = '/attractions/list';  // 返回景點列表頁
                },
                error: function(error) {
                    alert('新增失敗');
                }
            });
        });
    </script>
</body>
</html>
