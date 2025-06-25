package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//file:D:/myprojectImg	/artwork/	/avatar/
//file:E:/HTMLCSSJavaScript/myprojectImg	/artwork/	/avatar/
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 作品圖片
		registry.addResourceHandler("/myprojectImg/artwork/**")
				.addResourceLocations("file:D:/myprojectImg/artwork/");

		// 用戶頭像
		registry.addResourceHandler("/myprojectImg/avatar/**")
				.addResourceLocations("file:D:/myprojectImg/avatar/");
	}
}