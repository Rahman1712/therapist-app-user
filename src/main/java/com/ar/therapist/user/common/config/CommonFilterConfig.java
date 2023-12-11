package com.ar.therapist.user.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonFilterConfig {
	
	@Autowired private CommonJwtService commonJwtService;
	
	@Value("${filter.url.patterns}")
	private String[] URL_PATTERNS;
	
	@Bean
	public FilterRegistrationBean<CommonJwtFilter> customFilterRegistrationBean(){
		FilterRegistrationBean<CommonJwtFilter> registrationBean =
				new FilterRegistrationBean<>();
		registrationBean.setFilter(new CommonJwtFilter(commonJwtService));
		registrationBean.addUrlPatterns(URL_PATTERNS);  // "/*"
		return registrationBean;
	}
	
}


/*
	private final String[] URL_PATTERNS = {
			"/pckart/api/v1/brands/auth/*",
			"/pckart/api/v1/categories/auth/*",
			"/pckart/api/v1/products/auth/*",
	};
*/