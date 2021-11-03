package com.wtkj.oa.utils;

import com.wtkj.oa.common.config.ResponseMsg;

public class ResponseUtils {

    /**
     * 成功
     *
     * @param object
     * @return
     */
    public static ResponseMsg success(Object object) {
        return new ResponseMsg(200, "success", object);
    }

    public static ResponseMsg success() {
        return new ResponseMsg(200, "success", null);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static ResponseMsg error(Integer code, String message) {
        return new ResponseMsg(code, message, "failed");
    }
}
