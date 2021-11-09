package com.wtkj.oa.controller;

import com.wtkj.oa.common.config.ResponseMsg;
import com.wtkj.oa.service.InitDataService;
import com.wtkj.oa.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 初始化数据控制类
 * @Date 2021/11/9 10:42
 * @Author Peter.Chen
 */
@Api(description = "初始化数据")
@RestController
@RequestMapping("/init")
public class InitDataController {

    @Resource
    private InitDataService initDataService;

    @ApiOperation("批量导入公司")
    @PostMapping(value = "/company")
    public ResponseMsg initCompanies(@RequestParam(value = "file") MultipartFile file) {
        return ResponseUtils.success(initDataService.initCompanies(file));
    }

    @ApiOperation("批量导入知识产权")
    @PostMapping(value = "/patent")
    public ResponseMsg initPatents(@RequestParam(value = "file") MultipartFile file) {
        return ResponseUtils.success(initDataService.initPatents(file));
    }

    @ApiOperation("批量导入合同")
    @PostMapping(value = "/contract")
    public ResponseMsg initContracts(@RequestParam(value = "file") MultipartFile file) {
        return ResponseUtils.success(initDataService.initContracts(file));
    }

    @ApiOperation("导出模板")
    @GetMapping(value = "/exportExcel")
    public ResponseMsg initPatents(@RequestParam String fileType, HttpServletResponse response) {
        initDataService.exportExcel(fileType, response);
        return ResponseUtils.success();
    }
}
