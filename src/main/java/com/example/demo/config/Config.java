//package com.example.demo.config;
//
//import com.lib.payos.PayOS;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@SpringBootApplication
//
//public class Config implements WebMvcConfigurer {
//
//    public static String fe_liveServer_Success = "http://127.0.0.1:5555/olView/index.html#!/paymentSuccess";
//    public static String fe_liveServer_Failed = "http://127.0.0.1:5555/olView/index.html#!/paymentFailed";
//
//
//    @Value("${PAYOS_CLIENT_ID}")
//    private String clientId;
//
//    @Value("${PAYOS_API_KEY}")
//    private String apiKey;
//
//    @Value("${PAYOS_CHECKSUM_KEY}")
//    private String checksumKey;
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .exposedHeaders("*")
//                .allowCredentials(false)
//                .maxAge(3600); // Max age of the CORS pre-flight request
//    }
//    @Bean
//    public PayOS payOS() {
//        return new PayOS(clientId, apiKey, checksumKey);
//    }
//}
