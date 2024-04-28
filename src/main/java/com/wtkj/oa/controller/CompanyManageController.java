package com.wtkj.oa.controller;

import com.wtkj.oa.common.config.ResponseMsg;
import com.wtkj.oa.entity.Company;
import com.wtkj.oa.service.ICompanyManageService;
import com.wtkj.oa.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(description = "客户管理")
@RestController
@RequestMapping("/company")
@CrossOrigin(origins = "*")
public class CompanyManageController {

    @Resource
    private ICompanyManageService companyManageService;

    @ApiOperation(value = "添加客户")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseMsg addCompany(@RequestBody Company company) {
        companyManageService.addCompany(company);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "删除客户")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseMsg deleteCompany(@RequestParam String companyId) {
        companyManageService.deleteCompany(companyId);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "编辑客户")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseMsg updateCompany(@RequestBody Company company) {
        companyManageService.updateCompany(company);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "客户列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseMsg list(@RequestParam(required = false) String companyName,
                            @RequestParam(required = false) Integer agentFlag) {
        return ResponseUtils.success(companyManageService.list(companyName, agentFlag));
    }

    @ApiOperation(value = "客户列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseMsg list(@RequestBody Company company) {
        return ResponseUtils.success(companyManageService.list(company));
    }

    @ApiOperation(value = "代办事项公司")
    @RequestMapping(value = "/listByStatus", method = RequestMethod.GET)
    public ResponseMsg listByStatus() {
        return ResponseUtils.success(companyManageService.listByStatus());
    }
}
