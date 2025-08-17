package com.cloudSerenityHotel.order.service;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cloudSerenityHotel.order.model.Order;
import com.cloudSerenityHotel.order.model.OrderItems;
import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.service.UserService;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
@Transactional
public class EmailService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	@Autowired
    private JavaMailSender mailSender;
	@Autowired
    private UserService userService;

	/** 通用寄信方法 */
//    public void sendEmail(String to, String subject, String text) {
//    	try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(to);
//            message.setSubject(subject);
//            message.setText(text);
//            mailSender.send(message);
//            logger.info("Email 寄送成功給：{}", to);
//        } catch (Exception e) {
//            logger.error("Email 寄送失敗給 {}，原因：{}", to, e.getMessage(), e);
//        }
//    }
	public void sendEmail(String to, String subject, String text) {
	    try {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	        helper.setTo(to);
	        // ✅ 這裡一定要換成你的 Gmail，並可加中文顯示名稱
	        helper.setFrom(new InternetAddress("cloud.serenity.hotel@gmail.com", "伴手禮商城"));
	        helper.setSubject(subject);
	        helper.setText(text, false); // false = 純文字, true = HTML
	        mailSender.send(message);
	        logger.info("Email 寄送成功給：{}", to);
	    } catch (Exception e) {
	        logger.error("Email 寄送失敗給 {}，原因：{}", to, e.getMessage(), e);
	    }
	}

    
    /** 訂單成立通知（現金/非線上付款） */
    public void sendOrderCreatedEmail(Order dbOrder) {
        sendEmailToRecipientAndUser(dbOrder, false);
    }
    
    /** 訂單付款成功通知（信用卡） */
    public void sendPaymentSuccessEmail(Order dbOrder) {
        sendEmailToRecipientAndUser(dbOrder, true);
    }
    
    /** 自動判斷是否寄兩封 email */
    private void sendEmailToRecipientAndUser(Order dbOrder, boolean isPaid) {
        User user = userService.findMemberById(dbOrder.getUserId());
        if (user == null || user.getEmail() == null) {
            logger.error("使用者不存在或 email 為空，無法寄送郵件");
            return;}
        String userEmail = user.getEmail();
        String recipientEmail = dbOrder.getEmail(); // 訂單收件人 email
        String subject = isPaid ? "您的伴手禮商城訂單付款成功！" : "您的伴手禮商城訂單已成立！";
        String content = buildOrderEmailContent(dbOrder, user, isPaid);
        if (recipientEmail != null && !recipientEmail.equalsIgnoreCase(userEmail)) {
            // 收件人 email 與使用者不同 → 寄兩封
            sendEmail(recipientEmail, subject, content);
            sendEmail(userEmail, subject + "_備份", content);
        } else {
            // 相同 → 只寄一次
            sendEmail(userEmail, subject, content);
        }
    }

    /** 共用方法：生成郵件內容 */
    private String buildOrderEmailContent(Order dbOrder, User user, boolean isPaid) {
        StringBuilder itemList = new StringBuilder();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        for (OrderItems item : dbOrder.getOrderItems()) {
            BigDecimal unitPrice = item.getUnitPrice();
            BigDecimal specialPrice = item.getProducts().getSpecialPrice();
            BigDecimal discount = BigDecimal.ZERO;
            if (specialPrice != null && specialPrice.compareTo(BigDecimal.ZERO) > 0) {
                discount = unitPrice.subtract(specialPrice);}
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            BigDecimal itemDiscount = discount.multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            discountAmount = discountAmount.add(itemDiscount);
            itemList.append(String.format("%s (數量: %d, 單價: %s, 特價: %s, 折扣: %s)\n",
                    item.getProducts().getProductName(),
                    item.getQuantity(),
                    unitPrice.toString(),
                    specialPrice != null ? specialPrice.toString() : "無",
                    itemDiscount.toString()));
        }
        BigDecimal finalAmount = totalAmount.subtract(discountAmount);
        String paymentStatus = isPaid ? "付款已完成" : "待付款";
        return String.format(
                "親愛的 %s 您好，\n" +
                "您的伴手禮商城訂單狀態：%s\n" +
                "訂單編號：%s\n" +
                "商品清單：\n%s" +
                "訂單成立時間：%s\n" +
                "總金額：%s\n" +
                "折扣金額：%s\n" +
                "最終金額：%s\n" +
                "您可以點擊下方連結至會員中心查看訂單狀態：\n" +
                "http://localhost:5173/front/member/Order\n",
                user.getUserName(),
                paymentStatus,
                dbOrder.getOrderId(),
                itemList.toString(),
                dbOrder.getOrderDate(),
                totalAmount.toString(),
                discountAmount.toString(),
                finalAmount.toString()
        );
    }
    
}