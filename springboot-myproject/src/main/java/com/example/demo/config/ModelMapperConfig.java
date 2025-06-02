package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//做類型轉換
@Configuration // Springboot啟動前會先執行
public class ModelMapperConfig {
	
	// Springboot 自動創建並管理
	// 其他通過@autowired
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	
}
