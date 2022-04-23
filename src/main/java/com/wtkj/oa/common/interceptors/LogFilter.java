package com.wtkj.oa.common.interceptors;

import com.wtkj.oa.common.config.RepeatReadRequest;
import fr.opensagres.xdocreport.core.io.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //使用包装Request替换原始的Request
        request = new RepeatReadRequest(request);
        //读取流中的内容
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(request.getInputStream(), out);
        log.info("请求url:{}, 请求体:{}", request.getRequestURI(), out.toString(request.getCharacterEncoding()));
        filterChain.doFilter(request, response);
    }
}
