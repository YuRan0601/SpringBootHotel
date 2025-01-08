<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="zh-tw">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>查詢會員資料</title>
        <!-- 引入 jQuery -->
        <script src="https://code.jquery.com/jquery-3.7.1.js"
            integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>

        <!-- 引入 Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />

        <!-- 引入 Bootstrap Icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet" />

        <!-- 引入 Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="../../common/aside.css" />
        <style>
            .content {
                margin-left: 300px
            }
        </style>
        <script>
            $(function () {
                $(".aside").load("../../common/aside.html");

            });
        </script>
    </head>

    <body>
        <aside class="aside"></aside>
        <div class="content" align="center">
            <h2>查詢會員資料</h2>
            <form method="get" action="/CloudSerenityHotel/admin/queryAllMember">
                <button type="submit">查詢全部資料</button>
            </form>
            <form method="post" action="/CloudSerenityHotel/admin/queryMemberById">
                <p>以使用者編號查詢</p>
                <input type="number" min="1" name="id" placeholder="請輸入使用者編號" required />
                <button type="submit">查詢</button>
            </form>
            <form method="post" action="/CloudSerenityHotel/admin/queryMemberByName">
                <p>以使用者名稱查詢</p>
                <input type="text" name="name" placeholder="請輸入使用者名稱" required />
                <button type="submit">查詢</button>
            </form>
        </div>
    </body>

    </html>