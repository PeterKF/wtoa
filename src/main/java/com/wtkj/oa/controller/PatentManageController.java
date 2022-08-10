package com.wtkj.oa.controller;


import com.wtkj.oa.common.config.ResponseMsg;
import com.wtkj.oa.entity.Patent;
import com.wtkj.oa.service.IPatentManageService;
import com.wtkj.oa.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    @GetMapping(value = "/export/list")
    public ResponseMsg exportPatentList(@RequestParam List<String> patentIds, @RequestParam String companyType,
                                        HttpServletResponse response) {
        patentManageService.getPatentExpenseList(patentIds, companyType, response);
        return ResponseUtils.success();
    }

    @ApiOperation("获取专利清单内容")
    @GetMapping(value = "/patent/detail")
    public ResponseMsg getPatntDetail(@RequestParam String companyId, @RequestParam String companyType,
                                      @RequestParam List<String> patentIds) {
        return ResponseUtils.success(patentManageService.getPatentDetail(companyId, companyType, patentIds));
    }
}
