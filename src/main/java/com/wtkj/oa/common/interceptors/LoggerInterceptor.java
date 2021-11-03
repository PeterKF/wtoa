package com.wtkj.oa.common.interceptors;

import com.wtkj.oa.common.config.RepeatedlyReadRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;

/**
 * 将url请求的参数记录在日志中
 */
public class LoggerInterceptor implements HandlerInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.info("----------------------LoggerInterceptor---------------------");
        if (logger.isInfoEnabled()) {

            // 默认记录后台接口请求日志记录
            String url = request.getRequestURL().toString();
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String queryString = request.getQueryString();

            logger.info(String.format("请求参数, url: %s, method: %s, uri: %s, params: %s ", url, method, uri, queryString));

            RepeatedlyReadRequestWrapper requestWrapper;
            if (request instanceof RepeatedlyReadRequestWrapper) {
                // 签名处理过程 start....
                requestWrapper = (RepeatedlyReadRequestWrapper) request;
                if (requestWrapper.isBodyNotNull()) {
                    logger.info("请求Body: {} ", getBodyString(requestWrapper));
                }
                // 签名处理过程 end....
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 获取请求Body
     *
     * @param request
     * @return
     */
    public static String getBodyString(final ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = cloneInputStream(request.getInputStream());
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.error("getBodyString error:{}", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("getBodyString error:{}", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("getBodyString error:{}", e);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Description: 复制输入流</br>
     *
     * @param inputStream
     * @return</br>
     */
    public static InputStream cloneInputStream(ServletInputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            logger.error("cloneInputStream error:{}", e);
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return byteArrayInputStream;
    }
}
