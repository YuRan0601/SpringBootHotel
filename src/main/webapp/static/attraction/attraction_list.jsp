<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>景點管理</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <h1>景點管理</h1>
    <button onclick="location.href='./create_attraction.jsp'">新增景點</button>
    <table border="1" id="attractionsTable">
        <thead>
            <tr>
            
                <th>景點ID</th>
                <th>名稱</th>
                <th>描述</th>
                <th>位置</th>
                <th>開放時間</th>
                <th>聯絡資訊</th>
                <th>類型ID</th>
                <th>圖片URL</th>
                <th>建立時間</th>
                <th>更新時間</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <!-- 這裡會由AJAX動態填充 -->
        </tbody>
    </table>

    <script>
    // 不需要再使用 $(document).ready() 包裹 create 函數
    function create() {
        console.log('create function triggered');  // 確認是否觸發
        $.ajax({
            url: '/api/attractions/',  // AttractionController的GET請求URL
            type: 'GET',
            success: function(data) {
                console.log("data", data);
                let rows = '';
                data.forEach(function(attraction) {
                    rows += `<tr>
                                <td>${attraction.attractionId}</td>
                                <td>${attraction.name}</td>
                                <td>${attraction.description}</td>
                                <td>${attraction.location}</td>
                                <td>${attraction.openingHours}</td>
                                <td>${attraction.contactInfo}</td>
                                <td>${attraction.typeId}</td>
                                <td>${attraction.imageUrl}</td>
                                <td>${attraction.createAt}</td>
                                <td>${attraction.updateAt}</td>
                                <td>
                                    <button onclick="editAttraction(${attraction.attractionId})">編輯</button>
                                    <button onclick="deleteAttraction(${attraction.attractionId})">刪除</button>
                                </td>
                              </tr>`; 
                });
                $('#attractionsTable tbody').html(rows);
            },
            error: function(error) {
                console.log('Error:', error);
            }
        });
    }

    function editAttraction(id) {
        location.href = `/attractions/edit/${id}`;
    }

    function deleteAttraction(id) {
        if (confirm("確定刪除此景點嗎？")) {
            $.ajax({
                url: `/api/attractions/${id}`,
                type: 'DELETE',
                success: function(response) {
                    alert('景點已刪除');
                    location.reload();
                },
                error: function(error) {
                    alert('刪除失敗');
                }
            });
        }
    }
    </script>
</body>
</html>
