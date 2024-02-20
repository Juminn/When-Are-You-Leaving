package com.enm.costcalculrator.config;

import org.springframework.context.annotation.Configuration;
        import org.springframework.web.servlet.config.annotation.CorsRegistry;
        import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CORS 설정
        registry.addMapping("/**")
                .allowedOrigins("https://localhost:3000")
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins(
                        "https://d1y6ebwgoivo5n.cloudfront.net",
                        "http://d1y6ebwgoivo5n.cloudfront.net",
                        "https://www.d1y6ebwgoivo5n.cloudfront.net",
                        "http://www.d1y6ebwgoivo5n.cloudfront.net")
                .allowedOrigins(
                        "https://xn--ih3bt9oq0b6yi50k.com",
                        "http://xn--ih3bt9oq0b6yi50k.com",
                        "https://www.xn--ih3bt9oq0b6yi50k.com",
                        "http://www.xn--ih3bt9oq0b6yi50k.com");
    }
}