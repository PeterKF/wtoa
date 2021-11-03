package com.wtkj.oa.common.config;

import java.io.Serializable;

public class ResponseMsg<T> implements Serializable {

    private static final long serialVersionUID = 1926002052195800054L;

    private Integer status;

    private String message;

    private T result;

    public ResponseMsg() {
    }

    public ResponseMsg(Integer status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
