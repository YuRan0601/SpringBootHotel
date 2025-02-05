package com.cloudSerenityHotel.booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloudSerenityHotel.booking.dto.MonthlyBookingCount;
import com.cloudSerenityHotel.booking.model.BookingOrder;
import com.cloudSerenityHotel.booking.service.BookingService;
import com.cloudSerenityHotel.booking.vo.EcpayOrderVo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/booking")
@CrossOrigin(origins = {"http://localhost:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class BookingController {
	
	@Value("${ecpay.merchantId}")
    private String MERCHANT_ID; //測試用特店編號
	
	@Value("${ecpay.hashKey}")
    private String HASH_KEY; //測試用HASH_KEY
	
	@Value("${ecpay.hashIv}")
    private String HASH_IV;  //測試用HASH_IV
	
	@Value("${ecpay.paymentURL}")
    private String PAYMENT_URL; //ecpay付款測試環境網址
    
    @Value("${ngrok.baseURL}")
    private String NGROK_BASEURL; //ngrok對外暴露伺服器的網址
    
    @Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	BookingService bService;
	
	@PostMapping("/pay")
    public String createPayment(@RequestBody EcpayOrderVo order) {
        Map<String, String> params = new HashMap<>();
        
        BookingOrder dbOrder = bService.getOrderById(order.getOrderId());
        
        System.out.println(dbOrder.getStatus());
       
        if(dbOrder.getStatus().equals("pending")) {
        	String RETURN_URL = NGROK_BASEURL + "/CloudSerenityHotel/booking/return";

            String merchantTradeNo = order.getOrderId() + "t" + System.currentTimeMillis(); //訂單ID後加上時間戳，避免付款網頁意外關閉時無法再重新進入付款網頁
            
            params.put("MerchantID", MERCHANT_ID);
            params.put("MerchantTradeNo", merchantTradeNo);
            params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
            params.put("PaymentType", "aio");
            params.put("TotalAmount", order.getPrice().toString());
            params.put("TradeDesc", "信用卡支付");
            params.put("ItemName", order.getProductName());
            params.put("ReturnURL", RETURN_URL);
            params.put("ChoosePayment", "Credit");
            params.put("ClientBackURL", "http://localhost:5173/front/member/bookingOrder");
            params.put("CheckMacValue", generateCheckMacValue(params));

            StringBuilder form = new StringBuilder();
            form.append("<form id='ecpay-form' action='").append(PAYMENT_URL).append("' method='post'>");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                form.append("<input type='hidden' name='").append(entry.getKey()).append("' value='").append(entry.getValue()).append("'>");
            }
            form.append("<button type='submit'>前往付款</button>");
            form.append("</form>");
            form.append("<script>document.getElementById('ecpay-form').submit();</script>");

            return form.toString();
        } else if (dbOrder.getStatus().equals("confirmed")) {
        	return "1";
        } else if (dbOrder.getStatus().equals("cancelled")) {
        	return "2";
        } else {
        	return "3";
        }
        
    }

    @PostMapping("/return")
    public String paymentReturn(@RequestParam Map<String, String> responseParams) {
    	
    	String receivedCheckMacValue = responseParams.get("CheckMacValue");
    	
    	responseParams.remove("CheckMacValue");
    	
    	String myCheckMacValue = generateCheckMacValue(responseParams);

	    if (!myCheckMacValue.equalsIgnoreCase(receivedCheckMacValue)) {
	        System.out.println("驗證失敗");
	        return "CheckMacValue 驗證失敗";
	    }

        // 取得訂單號碼
        String orderId = responseParams.get("MerchantTradeNo").split("t")[0];
        String rtnCode = responseParams.get("RtnCode");
        

        if ("1".equals(rtnCode)) {
            // ✅ 付款成功，更新訂單狀態
        	System.out.println("付款成功");
        	
            bService.paymentSuccess(Integer.parseInt(orderId));
            BookingOrder dbOrder = bService.getOrderById(Integer.parseInt(orderId));
            
            SimpleMailMessage message = new SimpleMailMessage();
			
			String email = dbOrder.getUser().getEmail();
			
			//設定收件信箱
			//setTo也可以傳入String陣列，寄送信件到多個信箱
			message.setTo(email);
			
			//設定信件主旨(String)
			message.setSubject("您的訂房訂單付款成功!");
			
			String userName = dbOrder.getUser().getUserName();
			
			String roomType = dbOrder.getRoom().getRoomType().getTypeName();
			
			String checkInDate = dbOrder.getCheckInDate().toString();
			
			String checkOutDate = dbOrder.getCheckOutDate().toString();
			
			String totalPrice = dbOrder.getTotalPrice().toString();
			
			String content = String.format("""
					親愛的 %s 您好
					您的訂房訂單付款成功!
					訂單編號：%s
					預定房型：%s
					入住日期：%s
					退房日期：%s
					總金額：%s
					您可以點擊下方連結至會員中心查看訂單狀態
					http://localhost:5173/front/member/bookingOrder
					""", userName, orderId, roomType, checkInDate, checkOutDate, totalPrice);
			
			//設定信件內文(String)
			message.setText(content);
			
			//用JavaMailSender物件的send方法，把SimpleMailMessage物件傳入參數，送出信件
			mailSender.send(message);
            
            return "1";
        } else {
        	System.out.println("付款失敗");
        	return "0";
        }
        
    }

    private String generateCheckMacValue(Map<String, String> params) {
        String sortedParams = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        String raw = "HashKey=" + HASH_KEY + "&" + sortedParams + "&HashIV=" + HASH_IV;
        String encoded = urlEncode(raw).toLowerCase();
        return encryptMD5(encoded).toUpperCase();
    }

    private String urlEncode(String data) {
        try {
            return URLEncoder.encode(data, StandardCharsets.UTF_8.name())
                    .replace("%2D", "-")
                    .replace("%5F", "_")
                    .replace("%2E", ".")
                    .replace("%21", "!")
                    .replace("%2A", "*")
                    .replace("%28", "(")
                    .replace("%29", ")");
        } catch (Exception e) {
            throw new RuntimeException("URL Encode 失敗", e);
        }
    }

    private String encryptMD5(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 加密失敗", e);
        }
    }
	
	@GetMapping("/order")
	public List<Map<String, Object>> getAllOrders() {
		return bService.getAllOrders();
	}
	
	@GetMapping("/order/like/{keyword}")
	public List<Map<String, Object>> getOrderLikeUserNameOrOrderIdByKeyword(@PathVariable String keyword) {
		return bService.getOrderLikeUserNameOrOrderIdByKeyword(keyword);
	}
	
	@GetMapping("/order/like/{userId}/{orderId}")
	public List<Map<String, Object>> getOrderByOrderIdAndUserId(@PathVariable Integer userId, @PathVariable Integer orderId) {
		return bService.getOrderByOrderIdAndUserId(userId, orderId);
	}
	
	@GetMapping("/order/{userId}/{status}")
	public List<Map<String, Object>> getOrderByUserIdAndStatus(@PathVariable Integer userId, @PathVariable String status) {
		return bService.getOrderByUserIdAndStatus(userId, status);
	}
	
	@GetMapping("/order/{userId}")
	public List<Map<String, Object>> getOrderByUserId(@PathVariable Integer userId) {
		return bService.getOrderByUserId(userId);
	}
	
	@GetMapping("/order/status/{status}")
	public List<Map<String, Object>> getOrderByStatus(@PathVariable String status) {
		return bService.getOrderByStatus(status);
	}
	
	
	@PostMapping("/order/{roomTypeId}")
	public Map<String, Object> insertOrder(@RequestBody BookingOrder order, @PathVariable Integer roomTypeId) {
		System.out.println(order.getUser().getUserId());
		System.out.println(order.getTotalPrice());
		System.out.println(roomTypeId);
		
		return bService.insertOrder(order, roomTypeId);
	}
	
	@PutMapping("/order/cancel/{orderId}")
	public Map<String, Object> cancelOrder(@PathVariable Integer orderId) {
		return bService.cancelOrder(orderId);
	}
	
	@PutMapping("/order/{roomTypeId}")
	public Map<String, Object> updateOrderAdmin(@RequestBody BookingOrder order, @PathVariable Integer roomTypeId) {
		
		return bService.updateOrderAdmin(order, roomTypeId);
	}
	
	@GetMapping("/monthly-count")
	public ResponseEntity<List<MonthlyBookingCount>> getMonthlyBookingCounts() {
        List<MonthlyBookingCount> result = bService.findMonthlyBookingCounts();
        return ResponseEntity.ok(result);
    }
	
	
}
