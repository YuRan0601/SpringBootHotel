/**
 * 
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


$(function () {
	$(".aside").load("aside");
	
});