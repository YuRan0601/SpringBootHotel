<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-tw">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>雲澄旅館-註冊會員</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.1/css/all.min.css" />
    <style>
        .nav-bgc {
            background-color: lightblue;
        }

        #twIdMsg,
        #passwordMsg {
            color: #c70000;
            font-weight: bold;
        }

        #errorMessage {
            padding: 10px 0;
            margin: 0;
            color: #c70000;
            font-weight: bold;
            visibility: hidden;
            text-align: center;
        }
    </style>
</head>

<body>
    <nav class="navbar navbar-light nav-bgc">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">雲澄旅館</a>
        </div>
    </nav>
    <div class="container">
        <form method="post" action="/CloudSerenityHotel/user/register">
            <h2 style="text-align: center; margin: 0">歡迎註冊</h2>
            <br />
            <label for="identity" class="form-label fs-5">身分別</label>
            <select class="form-select w-25" id="identity" required>
                <option selected value="native">本國人</option>
                <option value="foreigner">外國人</option>
            </select>
            <br />
            <label for="email" class="form-label fs-5">電子信箱</label>
            <input type="email" class="form-control w-25" id="email" name="email" placeholder="請輸入電子信箱" required />
            <br />
            <label for="password" class="form-label fs-5">密碼</label>
            <input type="password" class="form-control w-25" id="password" name="password" placeholder="請輸入密碼"
                minlength="8" maxlength="64" required />
            <span id="passwordMsg" style="visibility: hidden"><i class="fa-solid fa-circle-xmark"></i>
            </span>
            <br />
            <label for="name" class="form-label fs-5">姓名</label>
            <input type="text" class="form-control w-25" id="name" name="name" placeholder="請輸入姓名" required />
            <br />
            <label for="gender" class="form-label fs-5">性別</label>
            <select class="form-select w-25" id="gender" name="gender" required>
                <option value="Male">男</option>
                <option value="Female">女</option>
                <option value="Other">其他</option>
            </select>
            <br />
            <label for="birthday" class="form-label fs-5">生日</label>
            <input type="date" class="form-control w-25" id="birthday" name="birthday" required />
            <br />
            <label for="phone" class="form-label fs-5">行動電話</label>
            <input type="tel" class="form-control w-25" id="phone" name="phone" placeholder="請輸入行動電話" required />
            <br />
            <label for="personal-id-no" class="form-label fs-5">身分證字號</label>
            <input type="text" class="form-control w-25" id="personal-id-no" name="personal_id_no"
                placeholder="請輸入身分證字號" required />
            <span id="twIdMsg" style="visibility: hidden"><i class="fa-solid fa-circle-xmark"></i> 身分證字號錯誤</span>
            <br />
            <label for="country" class="form-label fs-5">國家</label>
            <select class="form-select w-25" id="country" name="country" required></select>
            <br />
            <div class="form-group"></div>
            <label for="address" class="form-label fs-5">聯絡地址</label>
            <input type="text" class="form-control w-50" id="address" name="address" minlength="10" placeholder="請輸入地址"
                required />
            <br />
            <label for="passport-no" class="form-label fs-5">護照號碼</label>
            <input type="text" class="form-control w-25" id="passport-no" name="passport_no" placeholder="請輸入護照號碼"
                required />
            <br />
            <button type="submit" class="btn btn-primary">註冊</button>
        </form>
        <p id="errorMessage">
            <i class="fa-solid fa-circle-xmark"></i>
            <% String errorMessage=(String) request.getAttribute("errorMessage"); if
        (errorMessage !=null) { out.print(errorMessage); } %>
        </p>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous">
    </script>
    <script src="https://code.jquery.com/jquery-3.7.1.js"
        integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
    <script>
        //國家列表api
        fetch("https://restcountries.com/v3.1/all")
            .then((response) => response.json())
            .then((data) => {
                const select = document.getElementById("country");
                data.sort((a, b) => a.name.common.localeCompare(b.name.common)); // 按國名排序
                data.forEach((country) => {
                    const option = document.createElement("option");
                    option.value = country.cca3; // 使用 ISO 3166-1 Alpha-3 代碼
                    option.textContent = country.name.common; // 國家名稱
                    if (country.cca3 === "TWN") {
                        // 讓預設選中的選項是台灣
                        option.selected = true;
                    }
                    select.appendChild(option);
                });
            })
            .catch((error) => console.error("Error fetching country data:", error));
        //處理錯誤訊息
        $(document).ready(function () {
            showErrorMsg(); // 在頁面加載時執行
        });

        function showErrorMsg() {
            let msg = $("#errorMessage").text().trim(); // 取得錯誤訊息
            if (msg.length > 0) {
                // 如果訊息不為空，顯示錯誤訊息
                $("#errorMessage").css("visibility", "visible");
            } else {
                // 如果沒有訊息，隱藏錯誤訊息
                $("#errorMessage").css("visibility", "hidden");
            }
        }

        //判斷身分別 移除必填屬性
        addEventListener("load", (e) => {
            // console.log("load done");
            let id = $("#identity").val();
            if (id == "native") {
                //本國人無須護照號碼也可訂房入住
                $("#passport-no").removeAttr("required");
                $("#personal-id-no").attr("required", true);
            } else {
                //外國人沒有身分證字號
                $("#personal-id-no").removeAttr("required");
                $("#passport-no").attr("required", true);
            }
        });
        $("#identity").change((e) => {
            let id = $("#identity").val();
            if (id == "native") {
                //本國人無須護照號碼也可訂房入住
                $("#passport-no").removeAttr("required");
                $("#personal-id-no").attr("required", true);
            } else {
                //外國人沒有身分證字號
                $("#personal-id-no").removeAttr("required");
                $("#passport-no").attr("required", true);
            }
        });

        //password
        $("#password").blur(function () {
            let content = $(this).val();
            let contentLength = $(this).val().length;
            let rule1 = new RegExp(/[a-z]/i);
            let rule2 = new RegExp(/[0-9]/);
            let rule3 = new RegExp(/[!@#$%^*]/);
            if (contentLength == 0) {
                $("#passwordMsg").html(
                    '<i class="fa-solid fa-circle-xmark"></i> 密碼不可為空白');
                $("#passwordMsg").css("visibility", "visible");
            } else if (contentLength < 8) {
                $("#passwordMsg").html(
                    '<i class="fa-solid fa-circle-xmark"></i> 密碼至少8個字');
                $("#passwordMsg").css("visibility", "visible");
            } else if (!rule1.test(content)) {
                $("#passwordMsg").html(
                    '<i class="fa-solid fa-circle-xmark"></i> 密碼必須包含英文');
                $("#passwordMsg").css("visibility", "visible");
            } else if (!rule2.test(content)) {
                $("#passwordMsg").html(
                    '<i class="fa-solid fa-circle-xmark"></i> 密碼必須包含數字');
                $("#passwordMsg").css("visibility", "visible");
            } else if (!rule3.test(content)) {
                $("#passwordMsg").html(
                    '<i class="fa-solid fa-circle-xmark"></i> 密碼必須包含特殊字元!@#$%^*');
                $("#passwordMsg").css("visibility", "visible");
            } else {
                $("#passwordMsg").css("visibility", "hidden");
            }
        });

        // twId
        $("#personal-id-no").blur(function () {
            let content = $(this).val();
            let result = validateTaiwanID(content);
            if (!result) {
                $("#twIdMsg").css("visibility", "visible");
            } else {
                $("#twIdMsg").css("visibility", "hidden");
            }
        });

        //身分證驗證器 by GPT
        function validateTaiwanID(id) {
            // 檢查格式：英文字母開頭，接著9個數字
            const regex = /^[A-Z][0-9]{9}$/;
            if (!regex.test(id)) {
                return false;
            }

            // 字母對應數字表
            const letterMapping = {
                A: 10,B: 11,C: 12,D: 13,E: 14,F: 15,
                G: 16,H: 17,I: 34,J: 18,K: 19,L: 20,
                M: 21,N: 22,O: 35,P: 23,Q: 24,R: 25,
                S: 26,T: 27,U: 28,V: 29,W: 32,X: 30,
                Y: 31,Z: 33,
            };

            // 將第一個字母轉換為數字並拆分成兩部分 ex:10 -> 1 0
            const firstChar = id[0];
            const numCode = letterMapping[firstChar];
            //第一個數字
            const firstDigit = Math.floor(numCode / 10);
            //第二個數字
            const secondDigit = numCode % 10;

            // 計算權重總和 乘數 1 9 8 7 6 5 4 3 2 1 1
            let sum = firstDigit + secondDigit * 9;
            for (let i = 1; i <= 8; i++) {
                sum += parseInt(id[i]) * (9 - i);
            }
            // 加上最後一位檢查碼
            sum += parseInt(id[9]);
            // 確認總和能被10整除
            return sum % 10 === 0;
        }
    </script>
</body>

</html>