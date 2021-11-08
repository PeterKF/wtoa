package com.wtkj.oa.controller;

import com.wtkj.oa.common.config.ResponseMsg;
import com.wtkj.oa.entity.Company;
import com.wtkj.oa.entity.Contract;
import com.wtkj.oa.entity.ContractDate;
import com.wtkj.oa.service.IContractDetailsService;
import com.wtkj.oa.service.IContractManageService;
import com.wtkj.oa.service.IHTContractService;
import com.wtkj.oa.service.impl.TaskMatterServiceImpl;
import com.wtkj.oa.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(description = "合同管理")
@RestController
@RequestMapping("/contract")
public class ContractManageController {
    @Resource
    private IContractManageService contractManageService;

    @Resource
    private IHTContractService htContractService;

    @Resource
    private IContractDetailsService contractDetailsService;

    @Resource
    private TaskMatterServiceImpl taskMatterService;

    @ApiOperation(value = "读取word文件内容")
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public ResponseMsg readWord(@RequestParam Integer businessType, @RequestParam String contractType) {
        return ResponseUtils.success(contractManageService.getWordContent(businessType, contractType));
    }

    @ApiOperation("添加合同")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseMsg add(@RequestBody Contract contract) {
        contractManageService.addContract(contract);
        return ResponseUtils.success();
    }

    @ApiOperation("删除合同")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseMsg delete(@RequestParam String contractId) {
        contractManageService.delete(contractId);
        return ResponseUtils.success();
    }

    @ApiOperation("编辑合同")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseMsg update(@RequestBody Contract contract) {
        contractManageService.update(contract);
        return ResponseUtils.success();
    }

    @ApiOperation("合同列表页")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseMsg list(@RequestBody Contract contract) {
        return ResponseUtils.success(contractManageService.list(contract));
    }

    @ApiOperation("根据不同类型显示不同合同内容")
    @RequestMapping(value = "/getHtmlByType", method = RequestMethod.GET)
    public ResponseMsg getHtmlContent(@RequestParam String companyId, @RequestParam(required = false) Integer companyType,
                                      @RequestParam Integer businessType, @RequestParam String contractType) {
        return ResponseUtils.success(htContractService.getContentByType(companyId, companyType, businessType, contractType));
    }

    @ApiOperation("预览合同")
    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public ResponseMsg preview(@RequestParam String contractId) {
        return ResponseUtils.success(contractManageService.preview(contractId));
    }

    @ApiOperation("下载word文件")
    @RequestMapping(value = "/downLoadWord", method = RequestMethod.GET)
    public ResponseMsg downLoadWord(@RequestParam String contractId, HttpServletRequest request,
                                    HttpServletResponse response) {
        contractManageService.downLoadWord(contractId, request, response);
        return ResponseUtils.success();
    }

    @ApiOperation("合同列表根据公司名称分组")
    @RequestMapping(value = "/listByName", method = RequestMethod.GET)
    public ResponseMsg listByName(@RequestParam String userId, @RequestParam(required = false) String compnayName) {
        return ResponseUtils.success(contractManageService.listByName(userId, compnayName));
    }

    @ApiOperation("合同列表根据公司名称分组")
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public ResponseMsg detailList(@RequestParam String companyId) {
        return ResponseUtils.success(contractDetailsService.list(companyId));
    }

    @ApiOperation("预览文件")
    @RequestMapping(value = "/viewFile", method = RequestMethod.GET)
    public ResponseMsg viewFile(HttpServletResponse response, @RequestParam String contractId) {
        contractDetailsService.viewFile(response, contractId);
        return ResponseUtils.success();
    }

    @ApiOperation("更新状态")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public ResponseMsg updateStatus(@RequestBody ContractDate contractDate) {
        contractDetailsService.updateStatus(contractDate);
        return ResponseUtils.success();
    }

    @ApiOperation("任务事项")
    @RequestMapping(value = "/taskList", method = RequestMethod.POST)
    public ResponseMsg taskList(@RequestBody ContractDate contractDate) {
        return ResponseUtils.success(taskMatterService.taskList(contractDate));
    }

    @ApiOperation("客户列表")
    @PostMapping(value = "/company/list")
    public ResponseMsg companyList(@RequestBody Company company) {
        return ResponseUtils.success(contractManageService.companyList(company));
    }

    @ApiOperation("根据客户id展示不同的合同列表")
    @GetMapping(value = "/listByCompanyId")
    public ResponseMsg contractsByCompanyId(@RequestParam String companyId) {
        return ResponseUtils.success(contractManageService.contractsByCompanyId(companyId));
    }

    @ApiOperation("上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseMsg uploadImage(@RequestParam(value = "file") MultipartFile file, @RequestParam String contractId) {
        contractDetailsService.uploadFile(file, contractId);
        return ResponseUtils.success();
    }

    @ApiOperation("批量导入公司")
    @RequestMapping(value = "/upload/company", method = RequestMethod.POST)
    public ResponseMsg initCompanies(@RequestParam(value = "file") MultipartFile file) {
        return ResponseUtils.success(contractDetailsService.initCompanies(file));
    }

    @ApiOperation("批量导入知识产权")
    @PostMapping(value = "/upload/patent")
    public ResponseMsg initPatents(@RequestParam(value = "file") MultipartFile file) {
        return ResponseUtils.success(contractDetailsService.initPatents(file));
    }

    @ApiOperation("导出模板")
    @GetMapping(value = "/exportExcel")
    public ResponseMsg initPatents(@RequestParam String fileType, HttpServletResponse response) {
        contractDetailsService.exportExcel(fileType, response);
        return ResponseUtils.success();
    }
}
