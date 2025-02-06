package com.cloudSerenityHotel.attraction_facility.attraction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private JavaMailSender emailSender;

    @PostMapping("/send")
    public String sendEmail(@RequestParam String firstname,
                            @RequestParam String lastname,
                            @RequestParam String email,
                            @RequestParam String phone,
                            @RequestParam String message) {
        // 創建郵件內容
        String subject = "來自 " + firstname + " " + lastname + " 的新訊息";
        String body = "來自 " + firstname + " " + lastname + " 的訊息：\n\n";
        body += "電子郵件: " + email + "\n";
        body += "電話: " + phone + "\n";
        body += "訊息:\n" + message;

        // 發送郵件
        sendEmailToAdmin(subject, body);

        return "訊息已成功發送!";
    }

    private void sendEmailToAdmin(String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("cloud.serenity.hotel@gmail.com"); // 目標郵件地址
        mailMessage.setSubject(subject); // 郵件主題
        mailMessage.setText(body); // 郵件內容
        mailMessage.setFrom("no-reply@yourdomain.com"); // 發件人地址

        emailSender.send(mailMessage); // 發送郵件
    }
}
