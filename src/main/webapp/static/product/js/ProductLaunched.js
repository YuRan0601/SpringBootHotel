/**
 * 
 */
document.getElementById("upload").addEventListener("change", function () {
    const fileCount = this.files.length;
    const text = document.getElementById("uploadImageText");
    text.textContent = fileCount > 0 ? `已上傳${fileCount}張圖片` : "上傳圖片";
})

$(function () {
    $(".aside").load("http://localhost:8080/CloudSerenityHotel/aside", () => {
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

