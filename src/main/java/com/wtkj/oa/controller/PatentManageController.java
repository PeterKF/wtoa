package com.wtkj.oa.controller;


import com.wtkj.oa.common.config.ResponseMsg;
import com.wtkj.oa.entity.Patent;
import com.wtkj.oa.service.IPatentManageService;
import com.wtkj.oa.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(description = "专利管理")
@RestController
@RequestMapping("/patent")
public class PatentManageController {

    @Resource
    private IPatentManageService patentManageService;

    @ApiOperation("添加专利")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseMsg add(@RequestBody Patent patent) {
        patentManageService.addPatent(patent);
        return ResponseUtils.success();
    }

    @ApiOperation("删除专利")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseMsg add(@RequestParam String patentId) {
        patentManageService.delete(patentId);
        return ResponseUtils.success();
    }

    @ApiOperation("编辑专利")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseMsg update(@RequestBody Patent patent) {
        patentManageService.update(patent);
        return ResponseUtils.success();
    }

    @ApiOperation("专利列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseMsg list(@RequestBody Patent patent) {
        return ResponseUtils.success(patentManageService.list(patent));
    }

    @ApiOperation("导出专利清单")
    @PostMapping(value = "/export/list")
    public ResponseMsg exportPatentList(List<String> patentIds, String cpmpanyType) {
        // patentManageService.getPatentExpenseList(patentIds, cpmpanyType);
        return ResponseUtils.success();
    }
}
