package com.cf.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class SecurityConfiguration implements WebMvcConfigurer {

	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/securityHome").setViewName("hrHome");
        registry.addViewController("/failureUrl").setViewName("interviewerHome");
//        registry.addViewController("/login").setViewName("login");
    }
}
