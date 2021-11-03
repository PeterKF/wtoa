package com.wtkj.oa.common.config;

import com.wtkj.oa.common.interceptors.CommonIntercepter;
import com.wtkj.oa.common.interceptors.LoggerInterceptor;
import com.wtkj.oa.common.interceptors.RepeatedlyReadFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(getLoggerInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(getCommonInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    public HandlerInterceptor getLoggerInterceptor(){
        return new LoggerInterceptor();
    }

    @Bean
    public HandlerInterceptor getCommonInterceptor(){
        return new CommonIntercepter();
    }

    @Bean
    public FilterRegistrationBean repeatedlyReadFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        RepeatedlyReadFilter repeatedlyReadFilter = new RepeatedlyReadFilter();
        registration.setFilter(repeatedlyReadFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
