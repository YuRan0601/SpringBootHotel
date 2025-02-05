package com.cloudSerenityHotel.order.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.order.model.Order;
import com.cloudSerenityHotel.order.model.OrderItems;

@Service
public class EmailService {
	@Autowired
    private JavaMailSender mailSender;

	// 通用的發送郵件方法
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    // 專門處理訂單付款成功後發送郵件的邏輯
    public void sendPaymentSuccessEmail(Order dbOrder) {
        SimpleMailMessage message = new SimpleMailMessage();

        // 設置郵件收件人、主旨和內容
        String email = dbOrder.getUser().getEmail();
        message.setTo(email);
        message.setSubject("您的伴手禮商城訂單付款成功!");

        // 生成商品清單
        StringBuilder itemList = new StringBuilder();
        BigDecimal totalAmount = BigDecimal.ZERO;  // 總金額
        BigDecimal discountAmount = BigDecimal.ZERO; // 總折扣

        for (OrderItems item : dbOrder.getOrderItemsBeans()) {
            String productName = item.getProducts().getProductName();
            int quantity = item.getQuantity();
            BigDecimal unitPrice = item.getUnitPrice();
            BigDecimal specialPrice = item.getProducts().getSpecialPrice();
            BigDecimal discount = unitPrice.subtract(specialPrice != null ? specialPrice : BigDecimal.ZERO);

            // 計算每個商品的總價和折扣
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            BigDecimal itemDiscount = discount.multiply(BigDecimal.valueOf(quantity));

            // 累加總金額和折扣金額
            totalAmount = totalAmount.add(itemTotal);
            discountAmount = discountAmount.add(itemDiscount);

            // 生成每個商品的訊息
            itemList.append(String.format("%s (數量: %d, 單價: %s, 特價: %s, 折扣: %s)\n",
                    productName, quantity, unitPrice, specialPrice != null ? specialPrice : "無", itemDiscount));
        }

        // 計算最終金額
        BigDecimal finalAmount = totalAmount.subtract(discountAmount);

        // 設置郵件內容
        String userName = dbOrder.getUser().getUserName();
        String orderId = dbOrder.getOrderId().toString();
        String orderDate = dbOrder.getOrderDate().toString(); // 訂單成立時間

        // 確保顯示總金額無點數折抵的情況
        String content = String.format("""
                親愛的 %s 您好，
                您的伴手禮商城訂單付款成功！
                訂單編號：%s
                商品清單：
                %s
                訂單成立時間：%s
                總金額：%s
                折扣金額：%s
                最終金額：%s
                您可以點擊下方連結至會員中心查看訂單狀態：
                http://localhost:5173/front/member/Order
                """, userName, orderId, itemList.toString(), orderDate, totalAmount, discountAmount, finalAmount);

        message.setText(content);

        // 發送郵件
        mailSender.send(message);
    }
}