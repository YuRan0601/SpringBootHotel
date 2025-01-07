<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>修改管理員資料</title>
        <!-- 引入 jQuery -->
        <script src="https://code.jquery.com/jquery-3.7.1.js"
            integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>

        <!-- 引入 Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />

        <!-- 引入 Bootstrap Icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet" />

        <!-- 引入 Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="../static/common/aside.css" />
        <style>
            .content {
                margin-left: 300px
            }
        </style>
        <script>
            $(function () {
                $(".aside").load("../static/common/aside.html");

            });
        </script>
    </head>

    <body>
        <aside class="aside"></aside>
        <div class="content" align="center">
            <h2>修改管理員資料</h2>
            <form method="post" action="/CloudSerenityHotel/user/updateData">
                <label for="userId">使用者編號</label>
                <input type="text" id="userId" name="userId" value="${userData.userId}" readonly required>
                <p></p>
                <label for="name">使用者名稱</label>
                <input type="text" id="name" name="name" value="${userData.userName}" required>
                <p></p>
                <label for="email">電子信箱</label>
                <input type="email" id="email" name="email" value="${userData.email}" required>
                <p></p>
                <label for="password">密碼</label>
                <input type="text" id="password" name="password" value="${userData.password}" required>
                <p></p>
                <input type="hidden" id="identity" name="identity" value="admin" readonly>
                <button type="submit">修改</button>
            </form>
        </div>
    </body>

    </html>