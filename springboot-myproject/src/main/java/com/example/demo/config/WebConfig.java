package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



//file:D:/myprojectImg/
//file:E:/HTMLCSSJavaScript/myprojectImg/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/myprojectImg/**")
                .addResourceLocations("file:D:/myprojectImg/artwork/");	
    }
}