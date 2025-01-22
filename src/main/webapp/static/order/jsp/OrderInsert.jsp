<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ page pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html lang="zh-TW">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>新增訂單</title>
            <link rel="stylesheet" href="/CloudSerenityHotel/static/order/css/xxx.css" />

            <!-- 引入 jQuery -->
            <script src="https://code.jquery.com/jquery-3.7.1.js"
                integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>

            <!-- 引入 Bootstrap CSS -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />

            <!-- 引入 Bootstrap Icons -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet" />

            <!-- 引入 Bootstrap JS -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

            <!-- 引入aside.css -->
            <link rel="stylesheet" href="/CloudSerenityHotel/static/common/aside.css" />
            <script>
                $(function () {
                    $(".aside").load("/CloudSerenityHotel/static/common/aside.html", () => {
                        $.ajax({
                            type: "GET",
                            dataType: "json",
                            url: "http://localhost:8080/CloudSerenityHotel/user/getUserInfo",
                            success: (resp) => {
                                if (resp.code === 505) {
                                    alert("請先登入!");
                                    window.location.href =
                                        "http://localhost:8080/CloudSerenityHotel/static/user/login.jsp";
                                } else if (resp.code === 404) {
                                    alert("找不到此帳號!");
                                    window.location.href =
                                        "http://localhost:8080/CloudSerenityHotel/static/user/login.jsp";
                                } else {
                                    if (resp.data.userIdentity != "admin") {
                                        alert("你不是管理員，無權使用此介面");
                                        window.location.href =
                                            "http://localhost:8080/CloudSerenityHotel/user/logout";
                                        return;
                                    }

                                    $("#username").text(`歡迎 ${resp.data.userName} 管理員`);
                                    $("#loginUser").append(`<div><a href="http://localhost:8080/CloudSerenityHotel/user/logout">登出</a></div>`);
                                }
                            },
                        });
                    });

                });
            </script>
            <style>
                /* 設置整體背景色為淡黃色 */
                body {
                    font-family: Arial, sans-serif;
                    background-color: #fdf5e6;
                    /* 淡黃色背景 */
                    text-align: center;
                    padding: 20px;
                }

                /* 主要內容的樣式，假設class名為content */
                .content {
                    margin-left: 300px
                }

                /* 表單區塊的樣式 */
                form {
                    background-color: #ffffff;
                    padding: 20px;
                    border-radius: 10px;
                    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                    max-width: 600px;
                    margin: 0 auto;
                }

                h2 {
                    color: #1162be;
                }

                /* 表單標籤與輸入框的樣式 */
                div {
                    margin-bottom: 15px;
                    text-align: left;
                }

                label {
                    font-weight: bold;
                    margin-bottom: 5px;
                    display: block;
                    color: #333;
                }

                input[type="text"],
                input[type="number"],
                input[type="email"],
                input[type="submit"] {
                    width: 100%;
                    padding: 10px;
                    border: 1px solid #ccc;
                    border-radius: 5px;
                    font-size: 14px;
                    margin-top: 5px;
                }

                input[type="submit"] {
                    background-color: #1162be;
                    color: white;
                    cursor: pointer;
                }

                input[type="submit"]:hover {
                    background-color: #357ac8;
                }

                h3 {
                    margin-top: 30px;
                    color: #1162be;
                }

                /* 按鈕的樣式 */
                button {
                    background-color: #ECF5FF;
                    color: #1162be;
                    padding: 10px 15px;
                    font-weight: bold;
                    border-radius: 10px;
                    border: none;
                    cursor: pointer;
                }

                button:hover {
                    background-color: #357ac8;
                    color: #ffffff;
                }
            </style>
            <script>
                $.ajax({
                    url: '/getProductById',
                    type: 'GET',
                    data: { productId: productId },
                    success: function (response) {
                        console.log("Response from server:", response);
                        if (response.price) {
                            $('#unitPrice').val(response.price);
                        } else {
                            alert('無法獲取商品價格');
                        }
                    },
                    error: function () {
                        alert('獲取商品資訊時發生錯誤');
                    }
                });

            </script>
        </head>

        <body>
            <aside class="aside"></aside>

            <div class="content">
                <h2>新增訂單</h2>
                <form action="/CloudSerenityHotel/Order/add" method="POST">
                    <!-- 訂單基本資料 -->
                    <div>
                        <label for="userId">使用者 ID:</label>
                        <input type="number" id="userId" name="userId" required>
                    </div>
                    <div>
                        <label for="receiveName">收件人:</label>
                        <input type="text" id="receiveName" name="receiveName" required>
                    </div>
                    <div>
                        <label for="email">email:</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                    <div>
                        <label for="phoneNumber">電話號碼:</label>
                        <input type="text" id="phoneNumber" name="phoneNumber">
                    </div>
                    <div>
                        <label for="address">收貨地址:</label>
                        <input type="text" id="address" name="address" required>
                    </div>
                    <div>
                        <label for="orderStatus">訂單狀態:</label>
                        <!-- <input type="text" id="orderStatus" name="orderStatus" required>-->
                        <select name="orderStatus" multiple size="1" required>
                            <option value="處理中">處理中</option>
                            <option value="已出貨">已出貨</option>
                            <option value="已到貨">已到貨</option>
                            <option value="異常">異常</option>
                        </select>
                    </div>
                    <div>
                        <label for="paymentMethod">付款方式:</label>
                        <!-- <input type="text" id="paymentMethod" name="paymentMethod" required>-->
                        <select name="paymentMethod" multiple size="1" required>
                            <option value="信用卡">信用卡</option>
                            <option value="Line_Pay">Line_Pay</option>
                            <option value="悠遊付">悠遊付</option>
                            <option value="貨到付款">貨到付款</option>
                        </select>
                    </div>
                    <div>
                        <label for="totalAmount">總金額:</label>
                        <input type="number" id="totalAmount" name="totalAmount" required>
                    </div>
                    <div>
                        <label for="pointsDiscount">點數折抵:</label>
                        <input type="number" step="0.01" id="pointsDiscount" name="pointsDiscount">
                    </div>
                    <div>
                        <label for="discountAmount">折扣金額:</label>
                        <input type="number" step="0.01" id="discountAmount" name="discountAmount">
                    </div>
                    <div>
                        <label for="finalAmount">最終金額:</label>
                        <input type="number" step="0.01" id="finalAmount" name="finalAmount" required>
                    </div>

                    <!-- 訂單細項 -->
                    <h3 class="text-primary">訂單細項</h3>
                    <div class="mb-3">
                        <label for="productSelect" class="form-label">選擇商品:</label>
                        <select id="productSelect" name="productId" class="form-select" required>
                            <option value="">請選擇商品</option>
                            <option value="1">商品 1</option>
                            <option value="2">商品 2</option>
                            <option value="3">商品 3</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="quantity" class="form-label">數量:</label>
                        <input type="number" id="quantity" name="quantity" class="form-control" placeholder="數量"
                            required>
                    </div>
                    <div class="mb-3">
                        <label for="unitPrice" class="form-label">單價:</label>
                        <input type="number" id="unitPrice" step="0.01" name="unitPrice" class="form-control"
                            placeholder="單價" required readonly>
                    </div>
                    <div class="mb-3">
                        <label for="totalAmount" class="form-label">總金額:</label>
                        <input type="number" id="totalAmount" step="0.01" name="totalAmount" class="form-control"
                            placeholder="總金額" required readonly>
                    </div>

                    <div class="text-center">
                        <button type="submit" class="btn btn-primary">提交訂單</button>
                    </div>
                </form>
            </div>
        </body>

        </html>