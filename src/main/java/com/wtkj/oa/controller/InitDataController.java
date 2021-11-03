package com.wtkj.oa.controller;

import com.wtkj.oa.common.config.ResponseMsg;
import com.wtkj.oa.service.InitDataService;
import com.wtkj.oa.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Api(description = "初始化")
@RestController
@RequestMapping("/init")
public class InitDataController {

    @Resource
    private InitDataService initDataService;

    @ApiOperation("批量导入公司")
    @RequestMapping(value = "/company", method = RequestMethod.POST)
    public ResponseMsg initCompanies(@RequestParam(value = "file") MultipartFile file) {
        return ResponseUtils.success(initDataService.initCompanies(file));
    }

    @ApiOperation("批量导入知识产权")
    @PostMapping("/patent")
    public ResponseMsg initPatents(@RequestParam(value = "file", required = false) MultipartFile file) {
        return ResponseUtils.success(initDataService.initPatents(file));
    }
}
