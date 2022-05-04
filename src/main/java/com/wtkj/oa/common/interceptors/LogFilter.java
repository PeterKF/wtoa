package com.wtkj.oa.common.interceptors;

import cn.hutool.core.io.IoUtil;
import com.wtkj.oa.common.config.ContentCachingRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
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
        request = new ContentCachingRequestWrapper(request);
        //读取流中的内容
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ServletInputStream inputStream = request.getInputStream();
        byte[] buf = IoUtil.readBytes(inputStream, 1024);
        out.write(buf);
        log.info("请求url: {}, 请求方法: {}, 请求体: {}", request.getRequestURI(), request.getMethod(),
                out.toString(request.getCharacterEncoding()));
        IoUtil.read(inputStream);
        filterChain.doFilter(request, response);
    }

}
