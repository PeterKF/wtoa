package com.wtkj.oa.controller;

import com.wtkj.oa.entity.Role;
import com.wtkj.oa.entity.User;
import com.wtkj.oa.service.IUserManageService;
import com.wtkj.oa.common.config.ResponseMsg;
import com.wtkj.oa.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(description = "用户及角色管理")
@RestController
public class UserManageController {

    @Resource
    private IUserManageService userManageService;

    @ApiOperation(value = "添加角色")
    @RequestMapping(value = "/add/role", method = RequestMethod.POST)
    public ResponseMsg addRole(@RequestBody Role role) {
        userManageService.addRole(role);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "删除角色")
    @RequestMapping(value = "/delete/role", method = RequestMethod.POST)
    public ResponseMsg deleteRole(@RequestParam String roleId) {
        userManageService.deleteRole(roleId);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "编辑角色")
    @RequestMapping(value = "/update/role", method = RequestMethod.POST)
    public ResponseMsg updateRole(@RequestBody Role role) {
        userManageService.updateRole(role);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "角色列表")
    @RequestMapping(value = "/list/role", method = RequestMethod.GET)
    public ResponseMsg roleList() {
        return ResponseUtils.success(userManageService.roleList());
    }

    @ApiOperation(value = "添加用户")
    @RequestMapping(value = "/add/user", method = RequestMethod.POST)
    public ResponseMsg addUser(@RequestBody User user) {
        userManageService.addUser(user);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "删除用户")
    @RequestMapping(value = "/delete/user", method = RequestMethod.POST)
    public ResponseMsg deleteUser(@RequestParam String userId) {
        userManageService.deleteUser(userId);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "编辑用户")
    @RequestMapping(value = "/update/user", method = RequestMethod.POST)
    public ResponseMsg updateUser(@RequestBody User user) {
        userManageService.updateUser(user);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "用户列表")
    @RequestMapping(value = "/list/user", method = RequestMethod.POST)
    public ResponseMsg userList(@RequestBody User user) {
        return ResponseUtils.success(userManageService.userList(user));
    }

    @ApiOperation(value = "业务员列表")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseMsg userList() {
        return ResponseUtils.success(userManageService.commonUsers());
    }

    @ApiOperation(value = "根据角色名查用户")
    @RequestMapping(value = "/listByRole", method = RequestMethod.POST)
    public ResponseMsg listByRole(@RequestParam String roleName) {
        return ResponseUtils.success(userManageService.getUsersByRole(roleName));
    }

    @ApiOperation(value = "重置用户密码")
    @RequestMapping(value = "/user/reset", method = RequestMethod.GET)
    public ResponseMsg reset(@RequestParam String userId) {
        userManageService.reset(userId);
        return ResponseUtils.success();
    }
}
