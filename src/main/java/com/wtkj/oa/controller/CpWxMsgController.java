package com.wtkj.oa.controller;

import com.wtkj.oa.common.config.ResponseMsg;
import com.wtkj.oa.entity.CpWxMsg;
import com.wtkj.oa.service.impl.CpWxMsgServiceImpl;
import com.wtkj.oa.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@Api(description = "企业微信")
@RestController
@RequestMapping("/cpWx")
public class CpWxMsgController {

    public final CpWxMsgServiceImpl cpWxMsgService;

    public CpWxMsgController(CpWxMsgServiceImpl cpWxMsgService) {
        this.cpWxMsgService = cpWxMsgService;
    }

    @ApiOperation("添加配置项")
    @PostMapping("/addInfo")
    public ResponseMsg addInfo(@RequestBody CpWxMsg cpWxMsg) {
        cpWxMsgService.addInfo(cpWxMsg);
        return ResponseUtils.success();
    }

    @ApiOperation("获取token")
    @PostMapping("/getToken")
    public ResponseMsg getTokenByCache(@RequestBody CpWxMsg cpWxMsg) {
        return ResponseUtils.success(cpWxMsgService.getTokenByCache(cpWxMsg));
    }

    @ApiOperation("发送企业微信")
    @PostMapping("/sendMsg")
    public ResponseMsg sendMsg(@RequestBody CpWxMsg cpWxMsg) {
        return ResponseUtils.success(cpWxMsgService.sendMsg(cpWxMsg));
    }


}
