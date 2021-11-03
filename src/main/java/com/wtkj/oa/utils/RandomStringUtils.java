package com.wtkj.oa.utils;

import com.wtkj.oa.common.constant.ContractEnum;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自动生成特定字符串
 */
public class RandomStringUtils {
    private static final Object locker = new Object();

    public static String getNextVal() {
        synchronized (locker) {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
            StringBuilder builder = new StringBuilder();
            builder.append(formatDate.format(new Date()));
            return builder.append(Math.round(Math.random() * 899999999 + 100000000))
                    .toString();
        }
    }

    /**
     * 获得合同编号
     *
     * @param businessType
     * @param num
     * @return
     */
    public static String getContractCode(Integer businessType, Integer num) {
        synchronized (locker) {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
            StringBuffer builder = new StringBuffer();
            String code = String.valueOf(builder.append(formatDate.format(new Date()) + "WT"
                    + ContractEnum.getCodeByType(businessType) + String.format("%02d", Math.incrementExact(num))));
            return code;
        }
    }
}
