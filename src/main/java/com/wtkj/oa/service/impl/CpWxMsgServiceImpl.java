package com.wtkj.oa.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wtkj.oa.dao.CpWxMsgMapper;
import com.wtkj.oa.entity.CpWxMsg;
import com.wtkj.oa.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CpWxMsgServiceImpl extends ServiceImpl<CpWxMsgMapper, CpWxMsg> {

    private Cache<String, String> tokenCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();

    /**
     * 添加配置
     *
     * @param cpWxMsg
     */
    public void addInfo(CpWxMsg cpWxMsg) {
        this.save(cpWxMsg);
    }

    /**
     * 从缓存中获取token
     *
     * @param cpWxMsg
     * @return
     */
    public String getTokenByCache(CpWxMsg cpWxMsg) {
        String result = tokenCache.getIfPresent("token");
        if (StrUtil.isEmpty(result)) {
            result = this.getToken(cpWxMsg);
            tokenCache.put("token", result);
        }
        return result;
    }

    private String getToken(CpWxMsg cpWxMsg) {
        String getTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + cpWxMsg.getCorpid()
                + "&corpsecret=" + cpWxMsg.getCorpsecret();
        String token = "";
        try {
            token = HttpUtil.get(getTokenUrl, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            throw new BusinessException("token获取失败，{}" + e.getMessage());
        }
        if (StringUtil.isNotEmpty(token)) {
            Map<String, Object> map = new Gson().fromJson(token, new TypeToken<Map<String, Object>>() {
            }.getType());
            return map.get("access_token").toString();
        }
        return token;
    }

    /**
     * 发送企业微信
     *
     * @param cpWxMsg
     * @return
     */
    public String sendMsg(CpWxMsg cpWxMsg) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + this.getTokenByCache(cpWxMsg);
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("content", cpWxMsg.getContent());
        cpWxMsg.setText(contentMap);
        /*Map<String, Object> map = JSON.parseObject(JSON.toJSONString(cpWxMsg), new TypeReference<Map<String, Object>>() {
        });*/
        String result = "";
        try {
            result = HttpRequest.post(url)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .body(new Gson().toJson(cpWxMsg))
                    .timeout(20000)//超时，毫秒
                    .execute().body();
        } catch (HttpException e) {
            throw new BusinessException("企业微信发送失败，" + e.getMessage());
        }
        return result;
    }


}
