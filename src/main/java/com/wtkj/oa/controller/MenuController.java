package com.wtkj.oa.controller;

import com.wtkj.oa.common.config.ResponseMsg;
import com.wtkj.oa.entity.Menu;
import com.wtkj.oa.service.IMenuService;
import com.wtkj.oa.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(description = "菜单权限管理")
@RestController
@RequestMapping("/menu")
@CrossOrigin(origins = "*")
public class MenuController {
    @Resource
    private IMenuService menuService;

    @ApiOperation(value = "授权")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseMsg update(@RequestBody Menu menu) {
        menuService.update(menu);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "权限列表")
    @RequestMapping(value = "/listById", method = RequestMethod.GET)
    public ResponseMsg list(@RequestParam String roleId) {
        return ResponseUtils.success(menuService.getMenuByRoleId(roleId));
    }
}
