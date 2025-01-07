package com.cloudSerenityHotel.rent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{


	// 設置全局 CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 設置所有路徑都適用此 CORS 配置
                .allowedOrigins("http://127.0.0.1:5500")  // 設置允許的跨域來源
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 設置允許的 HTTP 方法
                .allowedHeaders("*")  // 設置允許的請求頭
                .allowCredentials(true);  // 設置是否允許攜帶憑證（如 Cookies）
    }
}
