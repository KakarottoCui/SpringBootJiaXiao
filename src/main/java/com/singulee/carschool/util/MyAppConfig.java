package com.singulee.carschool.util;

import com.singulee.carschool.interceptor.Interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyAppConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Interceptor loginInterceptor = new Interceptor();

        String[] path = {"/**"};
        String[] excludePath = {"/login.html", "/js/**", "/css/**", "/fonts/**", "/images/**", "/img/**", "/layui/**/**"};
        registry.addInterceptor(loginInterceptor).addPathPatterns(path).excludePathPatterns(excludePath);
    }
}