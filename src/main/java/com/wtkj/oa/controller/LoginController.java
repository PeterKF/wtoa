package com.wtkj.oa.controller;

import com.wtkj.oa.common.config.ResponseMsg;
import com.wtkj.oa.entity.User;
import com.wtkj.oa.service.ILoginService;
import com.wtkj.oa.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(description = "登录接口")
@RestController
@CrossOrigin(origins = "*")
public class LoginController {
    @Resource
    private ILoginService loginService;

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseMsg login(@RequestBody User user) {
        return ResponseUtils.success(loginService.login(user));
    }
}
