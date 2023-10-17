package com.hitwh.chemicalpark.config;

import com.hitwh.chemicalpark.config.interceptor.JWTInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/sensor")  //拦截所有请求，通过token判断
                .excludePathPatterns("/login","/user/login","/register","/user/register","/**/export","/**/import","/file/**","/schedule/**","/pic/**","/pic/snap/**","/position/getPositionData");
        }
    @Bean
    public JWTInterceptor jwtInterceptor(){
        return new JWTInterceptor();
    }
}
