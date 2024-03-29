package com.example.BE_PROJECT_OPEN_COLLAB.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/ws/**") // Adjust the mapping according to your endpoints
				.allowedOrigins("http://localhost:3000").allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowCredentials(true);
		registry.addMapping("/**").allowedOrigins("http://localhost:3000") // Replace with your frontend origin
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
	}
}
