package com.cloudSerenityHotel.booking.controller;

import org.springframework.web.bind.annotation.*;

import com.cloudSerenityHotel.booking.vo.EcpayOrderVo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/ecpay")
public class ECPAYController {

    private static final String MERCHANT_ID = "2000132";
    private static final String HASH_KEY = "5294y06JbISpM5x9";
    private static final String HASH_IV = "v77hoKGq4kWxNNIS";
    private static final String RETURN_URL = "https://localhost:8080/CloudSerenityHotel/ecpay/return";
    private static final String PAYMENT_URL = "https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5";

    @PostMapping("/pay")
    public String createPayment(@RequestBody EcpayOrderVo order) {
        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", MERCHANT_ID);
        params.put("MerchantTradeNo", "ORDER" + System.currentTimeMillis());
        params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        params.put("PaymentType", "aio");
        params.put("TotalAmount", order.getPrice().toString());
        params.put("TradeDesc", "信用卡支付");
        params.put("ItemName", order.getProductName());
        params.put("ReturnURL", RETURN_URL);
        params.put("ChoosePayment", "Credit");
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
        return "1".equals(responseParams.get("RtnCode")) ? "交易成功" : "交易失敗";
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
}

