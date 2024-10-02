package com.example.fpi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        이 경로로 요청이 들어오면
        registry.addResourceHandler("/uploads/**")
//                여기서 자동으로 매핑되어 이 폴더 안에서 찾음  **:모든 파일과 하위폴더를 포함한 경로
                .addResourceLocations("file:src/main/resources/static/uploads/");
    }
}
