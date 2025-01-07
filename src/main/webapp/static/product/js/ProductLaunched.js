/**
 * 
 */
document.getElementById("upload").addEventListener("change", function () {
    const fileCount = this.files.length;
    const text = document.getElementById("uploadImageText");
    text.textContent = fileCount > 0 ? `已上傳${fileCount}張圖片` : "上傳圖片";
})

$(function () {
	$(".aside").load("http://localhost:8080/CloudSerenityHotel/productHome");
	
});

