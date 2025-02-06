package com.cloudSerenityHotel.order.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloudSerenityHotel.order.dao.OrderDao;
import com.cloudSerenityHotel.order.dto.PaymentDTO;
import com.cloudSerenityHotel.order.model.Order;

@Service
public class PaymentService {
	
	@Autowired
	private OrderDao orderDao;  // 注入 OrderDao 來查詢 Order 實體
	
    @Autowired
    private EmailService emailService;          // 注入 EmailService

    /*@Autowired
    private OrderServiceImpl orderServiceImpl;  // 注入 OrderServiceImpl
     */
	
	@Value("${ecpay.merchantId}")
    private String MERCHANT_ID;

    @Value("${ecpay.hashKey}")
    private String HASH_KEY;

    @Value("${ecpay.hashIv}")
    private String HASH_IV;

    @Value("${ecpay.paymentURL}")
    private String PAYMENT_URL;

    @Value("${ngrok.baseURL}")
    private String NGROK_BASEURL;
    
    // 處理支付回調
    public void processPaymentReturn(Map<String, String> responseParams) {
    	// 取得回傳的 CheckMacValue
        String receivedCheckMacValue = responseParams.get("CheckMacValue");
        
        responseParams.remove("CheckMacValue");
        
        // 生成本地的 CheckMacValue
        String myCheckMacValue = generateCheckMacValue(responseParams);
        
        // 印出來，方便檢查
        System.out.println("回傳的 CheckMacValue: " + receivedCheckMacValue);
        System.out.println("生成本地的 CheckMacValue: " + myCheckMacValue);
        
        // 檢查 CheckMacValue 是否一致
        if (!myCheckMacValue.equalsIgnoreCase(receivedCheckMacValue)) {
            throw new RuntimeException("CheckMacValue 驗證失敗");
        }

        // 取得訂單 ID
        String orderId = responseParams.get("MerchantTradeNo").split("t")[0];
        String rtnCode = responseParams.get("RtnCode");

        // 根據支付結果更新訂單狀態
        if ("1".equals(rtnCode)) {
            // 支付成功，更新訂單狀態
            Order dbOrder = orderDao.findById(Integer.parseInt(orderId))
                    .orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + orderId));

            // 更新訂單狀態為已付款
            dbOrder.setOrderStatus("已付款");
            orderDao.save(dbOrder);  // 保存更新後的訂單

            // 發送郵件
            emailService.sendPaymentSuccessEmail(dbOrder);
        } else {
            throw new RuntimeException("支付失敗");
        }
    }

    // 用來生成 CheckMacValue 的方法
    private String generateCheckMacValue(Map<String, String> params) {
        String sortedParams = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        String raw = "HashKey=" + HASH_KEY + "&" + sortedParams + "&HashIV=" + HASH_IV;
        String encoded = urlEncode(raw).toLowerCase();
        return encryptMD5(encoded).toUpperCase();
    }

    // URL 編碼
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

    // MD5 加密
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

    // 處理訂單支付請求
    public String createPayment(PaymentDTO paymentDTO) {
        Map<String, String> params = new HashMap<>();

        // 直接通過 OrderDao 查詢 Order 實體
        Order dbOrder = orderDao.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + paymentDTO.getOrderId()));
        System.out.println(dbOrder.getOrderStatus());

        if (dbOrder.getOrderStatus().equals("未付款")) {
            String RETURN_URL = NGROK_BASEURL + "/CloudSerenityHotel/Order/paymentResult";

            String merchantTradeNo = paymentDTO.getOrderId() + "t" + System.currentTimeMillis(); // 訂單ID後加上時間戳
            
            // 設定參數
            params.put("MerchantID", MERCHANT_ID);
            params.put("MerchantTradeNo", merchantTradeNo);
            params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
            params.put("PaymentType", "aio");
            params.put("TotalAmount", paymentDTO.getFinalAmount().toString());
            params.put("TradeDesc", "信用卡支付");
            params.put("ItemName", paymentDTO.getProductName());
            params.put("ChoosePayment", "Credit");  // 付款方式
            params.put("ReturnURL", RETURN_URL);
            params.put("CheckMacValue", generateCheckMacValue(params));

            // 印出所有請求參數
            System.out.println("發送給綠界的請求參數：");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
            
            // 生成表單並發送
            StringBuilder form = new StringBuilder();
            form.append("<form id='ecpay-form' action='").append(PAYMENT_URL).append("' method='post'>");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                form.append("<input type='hidden' name='").append(entry.getKey()).append("' value='").append(entry.getValue()).append("'>");
            }
            form.append("<button type='submit'>前往付款</button>");
            form.append("</form>");
            form.append("<script>document.getElementById('ecpay-form').submit();</script>");

            return form.toString();
        } else {
            return "訂單狀態無法進行付款";
        }
    }
}