package com.wtkj.oa.common.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截前端请求
 */
@Component
public class CommonIntercepter implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(CommonIntercepter.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        logger.info("----------------------CommonIntercepter---------------------");
        //允许跨域,不能放在postHandle内
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization, X-Requested-With");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Max-Age", "3600");
        return true;

    }
}
