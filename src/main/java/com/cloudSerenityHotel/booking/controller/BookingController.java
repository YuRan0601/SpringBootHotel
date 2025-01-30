package com.cloudSerenityHotel.booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/booking")
@CrossOrigin(origins = {"http://localhost:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class BookingController {
	
    private static final String MERCHANT_ID = "2000132"; //測試用特店編號
    private static final String HASH_KEY = "5294y06JbISpM5x9"; //測試用HASH_KEY
    private static final String HASH_IV = "v77hoKGq4kWxNNIS";  //測試用HASH_IV
    private static final String RETURN_URL = "https://localhost:8080/CloudSerenityHotel/booking/return";  //付款後回傳訊息接收的controller url
    private static final String PAYMENT_URL = "https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5"; //綠界測試付款網址
	
	@Autowired
	BookingService bService;
	
	@PostMapping("/pay")
	@ResponseBody
    public String createPayment(@RequestBody EcpayOrderVo order) {
        Map<String, String> params = new HashMap<>();
        
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
        params.put("OrderResultURL", "http://localhost:5173/booking/paySuccess");
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
    }

    @PostMapping("/return")
    public String paymentReturn(@RequestParam Map<String, String> responseParams) {
    	String receivedCheckMacValue = responseParams.get("CheckMacValue");
        if (!generateCheckMacValue(responseParams).equalsIgnoreCase(receivedCheckMacValue)) {
            return "CheckMacValue 驗證失敗";
        }

        // 取得訂單號碼
        String orderId = responseParams.get("MerchantTradeNo").split("t")[0];
        String rtnCode = responseParams.get("RtnCode");

        String redirectUrl;
        if ("1".equals(rtnCode)) {
            // ✅ 付款成功，更新訂單狀態
            bService.paymentSuccess(Integer.parseInt(orderId));
            redirectUrl = "http://localhost:5173/booking/paySuccess";
        } else {
        	redirectUrl = "http://localhost:5173/booking/payFail";
        }
        
        return "<html><head><meta http-equiv='refresh' content='0;url=" + redirectUrl + "'></head></html>";
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
	@ResponseBody
	public List<Map<String, Object>> getAllOrders() {
		return bService.getAllOrders();
	}
	
	@GetMapping("/order/{userId}/{status}")
	@ResponseBody
	public List<Map<String, Object>> getOrderByUserIdAndStatus(@PathVariable Integer userId, @PathVariable String status) {
		return bService.getOrderByUserIdAndStatus(userId, status);
	}
	
	@GetMapping("/order/{userId}")
	@ResponseBody
	public List<Map<String, Object>> getOrderByUserId(@PathVariable Integer userId) {
		return bService.getOrderByUserId(userId);
	}
	
	
	@PostMapping("/order/{roomTypeId}")
	@ResponseBody
	public Map<String, Object> insertOrder(@RequestBody BookingOrder order, @PathVariable Integer roomTypeId) {
		System.out.println(order.getUser().getUserId());
		System.out.println(order.getTotalPrice());
		System.out.println(roomTypeId);
		
		return bService.insertOrder(order, roomTypeId);
	}
	
	@PutMapping("/order/cancel/{orderId}")
	@ResponseBody
	public Map<String, Object> cancelOrder(@PathVariable Integer orderId) {
		return bService.cancelOrder(orderId);
	}
	
	@PutMapping("/order/{roomTypeId}")
	@ResponseBody
	public Map<String, Object> updateOrderAdmin(@RequestBody BookingOrder order, @PathVariable Integer roomTypeId) {
		
		return bService.updateOrderAdmin(order, roomTypeId);
	}
}
