<!-- edit_attraction.jsp -->
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>編輯景點</title>
</head>
<body>
    <h1>編輯景點</h1>
    <form id="editAttractionForm">
        <input type="hidden" id="attractionId" name="attractionId">

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

        <button type="submit">更新</button>
    </form>

    <script>
        let attractionId = window.location.pathname.split('/').pop();  // 獲取ID
        $.ajax({
            url: `/api/attractions/${attractionId}`,
            type: 'GET',
            success: function(data) {
                document.getElementById('attractionId').value = data.attractionId;
                document.getElementById('name').value = data.name;
                document.getElementById('description').value = data.description;
                document.getElementById('location').value = data.location;
                document.getElementById('openingHours').value = data.openingHours;
                document.getElementById('contactInfo').value = data.contactInfo;
                document.getElementById('typeId').value = data.typeId;
                document.getElementById('imageUrl').value = data.imageUrl;
            },
            error: function(error) {
                alert('無法獲取資料');
            }
        });

        document.getElementById('editAttractionForm').addEventListener('submit', function(e) {
            e.preventDefault();
            let formData = {
                attractionId: document.getElementById('attractionId').value,
                name: document.getElementById('name').value,
                description: document.getElementById('description').value,
                location: document.getElementById('location').value,
                openingHours: document.getElementById('openingHours').value,
                contactInfo: document.getElementById('contactInfo').value,
                typeId: document.getElementById('typeId').value,
                imageUrl: document.getElementById('imageUrl').value
            };

            $.ajax({
                url: `/api/attractions/${attractionId}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function(data) {
                    alert('景點更新成功');
                    location.href = '/attractions/list';  // 返回景點列表頁
                },
                error: function(error) {
                    alert('更新失敗');
                }
            });
        });
    </script>
</body>
</html>
