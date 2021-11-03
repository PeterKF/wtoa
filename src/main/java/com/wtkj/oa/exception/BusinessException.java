package com.wtkj.oa.exception;

/**
 * 自定义异常类
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -2438347915833053042L;

    public BusinessException(String message) {
        super(message);
    }
}
