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
import org.springframework.transaction.annotation.Transactional;
import com.cloudSerenityHotel.order.dao.OrderDao;
import com.cloudSerenityHotel.order.dto.PaymentDTO;
import com.cloudSerenityHotel.order.model.Order;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderService orderService; 

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
    
    /**
     * 處理金流回調
     */
    public void processPaymentReturn(Map<String, String> responseParams) {
    	// 取得回傳的 CheckMacValue
        String receivedCheckMacValue = responseParams.get("CheckMacValue");
        responseParams.remove("CheckMacValue");
        // 生成本地的 CheckMacValue
        String myCheckMacValue = generateCheckMacValue(responseParams);
        // 印出來，方便檢查
//        System.out.println("回傳的 CheckMacValue: " + receivedCheckMacValue);
//        System.out.println("生成本地的 CheckMacValue: " + myCheckMacValue);
        // 檢查 CheckMacValue 是否一致
        if (!myCheckMacValue.equalsIgnoreCase(receivedCheckMacValue)) {
            throw new RuntimeException("CheckMacValue 驗證失敗");}
        // 取得訂單 ID
        String orderIdStr  = responseParams.get("MerchantTradeNo").split("t")[0];
        String rtnCode = responseParams.get("RtnCode");
     // 如果金流回傳成功，交給 OrderService 處理
        if ("1".equals(rtnCode)) {
            Integer orderId = Integer.parseInt(orderIdStr);
            orderService.paymentSuccess(orderId);
        } else {
            throw new RuntimeException("支付失敗，RtnCode=" + rtnCode);
        }
    }
    
    /**
     * 產生付款表單
     */
    public String createPayment(PaymentDTO paymentDTO) {
    	Order dbOrder = orderDao.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("訂單不存在，ID: " + paymentDTO.getOrderId()));
        if (!"未付款".equals(dbOrder.getOrderStatus())) {
            return "訂單狀態無法進行付款";}
        Map<String, String> params = new HashMap<>();
        String RETURN_URL = NGROK_BASEURL + "/CloudSerenityHotel/Order/paymentResult";
        String merchantTradeNo = paymentDTO.getOrderId() + "t" + System.currentTimeMillis();
        params.put("MerchantID", MERCHANT_ID);
        params.put("MerchantTradeNo", merchantTradeNo);
        params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        params.put("PaymentType", "aio");
        params.put("TotalAmount", paymentDTO.getFinalAmount().toString());
        params.put("TradeDesc", "信用卡支付");
        params.put("ItemName", paymentDTO.getProductName());
        params.put("ChoosePayment", "Credit");
        params.put("ReturnURL", RETURN_URL);
        params.put("ClientBackURL", "http://localhost:5173/front/member/Order");
        params.put("CheckMacValue", generateCheckMacValue(params));
        // 生成 HTML 表單
        StringBuilder form = new StringBuilder();
        form.append("<form id='ecpay-form' action='").append(PAYMENT_URL).append("' method='post'>");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            form.append("<input type='hidden' name='").append(entry.getKey())
                .append("' value='").append(entry.getValue()).append("'>");}
        form.append("<button type='submit'>前往付款</button>");
        form.append("</form>");
        form.append("<script>document.getElementById('ecpay-form').submit();</script>");
        return form.toString();
    }
    
    /* -------------------- 私有輔助方法 -------------------- */
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
}