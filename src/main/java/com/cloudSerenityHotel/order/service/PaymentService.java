package com.cloudSerenityHotel.order.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
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
}
