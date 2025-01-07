<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>操作失敗</title>
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
            <h1>${errorMessage} 操作失敗，請重新查詢後再操作!</h1>
            <div>
    </body>

    </html>