package com.wtkj.oa.common.interceptors;

import com.wtkj.oa.common.config.RepeatedlyReadRequestWrapper;
import org.apache.catalina.connector.RequestFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 复制请求数据包body
 * 以提供 拦截器下 可数次获取Body数据包
 */
public class RepeatedlyReadFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RepeatedlyReadFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("----------------------RepeatedlyReadFilter---------------------");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("复制request.getInputStream流");
        ServletRequest requestWrapper = null;
        String path = ((RequestFacade) request).getRequestURI();
        if (!path.contains("upload") && request instanceof HttpServletRequest) {
            requestWrapper = new RepeatedlyReadRequestWrapper((HttpServletRequest) request);
        }
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void destroy() {

    }
}