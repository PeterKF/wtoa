package com.wtkj.oa.common.interceptors;

import com.wtkj.oa.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端调用接口报错后，返回结果
 */
@RestControllerAdvice
public class CustomerExceptionHandler {
    private final static Logger LOG = LoggerFactory.getLogger(CustomerExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handlerBusinessException(BusinessException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", 500);
        result.put("result", "failed");
        result.put("message", e.getMessage());

        LOG.error("business error{}", e);
        return result;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handlerBusinessException(MethodArgumentNotValidException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", 400);
        result.put("result", "failed");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder("");
        if (!CollectionUtils.isEmpty(errors)) {
            for (FieldError er : errors) {
                sb.append(er.getDefaultMessage() + ";");
            }
        }
        result.put("message", sb.toString());

        LOG.error("business error{}", e);
        return result;
    }

    @ExceptionHandler(AccountException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handlerAccountException(AccountException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", 401);
        result.put("result", "login failed");
        result.put("message", e.getMessage());

        LOG.error("auth error{}", e);
        return result;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handlerAllException(Exception e) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", 500);
        result.put("result", "system error");
        result.put("message", "系统异常");

        LOG.error("system error{}", e);
        return result;
    }
}