/**
 * 執行Home.html，會自動跳轉到顯示全部商品
 */
window.location.href = '/CloudSerenityHotel/ProductGetAllController';


/*
*觸發上下架按鈕
*/
document.querySelectorAll('.productLaunched').forEach(button => {
    button.addEventListener('click', function () {
        if (this.textContent === '上架') {
            this.textContent = '下架';
            this.className = 'default';
        } else {
            this.textContent = '上架';
            this.className = 'productLaunched';
        }
    });
});