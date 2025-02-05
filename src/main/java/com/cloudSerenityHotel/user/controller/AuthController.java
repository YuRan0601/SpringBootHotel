package com.cloudSerenityHotel.user.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cloudSerenityHotel.user.model.PasswordResetToken;
import com.cloudSerenityHotel.user.model.PasswordResetTokenRepository;
import com.cloudSerenityHotel.user.model.User;
import com.cloudSerenityHotel.user.model.UserRepository;

@CrossOrigin(origins = {"http://localhost:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Controller
@RequestMapping("/user")
public class AuthController {
	@Autowired
	private UserRepository uRepository;
	
	@Autowired
	private PasswordResetTokenRepository prtRepository;
	
	@Autowired
	private JavaMailSender mailSender;

	//忘記密碼
	@PostMapping("/forgotPassword")
	public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
	    String email = request.get("email");
	    User user = uRepository.findByEmail(email).orElse(null);
	    
	    //如果該Email沒有註冊就不產生token和寄mail
	    if (user == null) {
	    	return ResponseEntity.ok("密碼重設郵件已發送");
	    }

	    // 產生 Token（有效期限 1 小時）
	    String token = UUID.randomUUID().toString();
	    PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusHours(1));
	    prtRepository.save(resetToken);

	    // 寄送 Email
	    
		//建立SimpleMailMessage物件
		SimpleMailMessage message = new SimpleMailMessage();
		
		//設定收件信箱
		//setTo也可以傳入String陣列，寄送信件到多個信箱
		message.setTo(user.getEmail());
		
		//設定信件主旨(String)
		message.setSubject("Cloud Serenity Hotel 雲澄旅館 重設密碼通知信");
		
		//設定信件內文(String)
		//重設密碼連結
		String resetLink = "http://localhost:5173/front/resetPassword?token=" + token;
		String msgString = "我們收到了您重設密碼的請求\n"+"請點擊以下連結重設密碼: \n" + resetLink + "\n此連結將於 1 小時後失效。" + "\n如果您沒有提出這個要求，請忽略此電子郵件。"+"\n Cloud Serenity Hotel 雲澄旅館©";
		message.setText(msgString);
		
		//用JavaMailSender物件的send方法，把SimpleMailMessage物件傳入參數，送出信件
		mailSender.send(message);

	    return ResponseEntity.ok("密碼重設郵件已發送");
	}
	
	//重設密碼
	@PostMapping("/resetPassword")
	public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
	    String token = request.get("token");
	    String newPassword = request.get("newPassword");

	    // 找到 Token
	    Optional<PasswordResetToken> resetToken = prtRepository.findByToken(token);

	    if (resetToken.isEmpty() || resetToken.get().getExpiryDate().isBefore(LocalDateTime.now())) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無效或過期的 Token");
	    }

	    // 更新密碼
	    User user = resetToken.get().getUser();
	    user.setPassword(newPassword);
	    uRepository.save(user);

	    // 刪除 Token
	    prtRepository.delete(resetToken.get());

	    return ResponseEntity.ok("密碼已更新");
	}
	
}
