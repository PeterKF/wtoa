package com.wtkj.oa.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.wtkj.oa.dao.ContentMapper;
import com.wtkj.oa.entity.*;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.ICompanyManageService;
import com.wtkj.oa.service.IContractManageService;
import com.wtkj.oa.service.IHTContractService;
import com.wtkj.oa.utils.YamlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class HTContractServiceImpl implements IHTContractService {

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private ICompanyManageService companyManageService;

    @Resource
    private IContractManageService contractManageService;

    /**
     * 根据业务类型和合同类型显示合同内容
     *
     * @param companyId
     * @param companyType
     * @param businessType
     * @param contractType
     * @return
     */
    @Override
    public ResultDTO getContentByType(String companyId, Integer companyType, Integer businessType, String contractType) {
        ResultDTO resultDTO = new ResultDTO();
        String result = "";
        StringBuilder contentStr = new StringBuilder();
        String[] types = contractType.split(",");
        if (businessType.equals(1)) {
            String body = contentMapper.getContentByType(businessType, "8").getContent();
            contentStr.append(body);
            for (int i = 0; i < types.length; i++) {
                Content content = contentMapper.getContentByType(businessType, types[i]);
                String title = content.getTitle().replace("{number}", String.valueOf(i + 1));
                contentStr.append(title).append(content.getContent());
            }
            body = contentMapper.getContentByType(businessType, "9").getContent();
            contentStr.append(body);
            Company company = companyManageService.selectOne(companyId);
            if (!ObjectUtils.isEmpty(company)) {
                result = contentStr.toString().replace("{companyName}", StrUtil.isEmpty(company.getCompanyName()) ? "" : company.getCompanyName())
                        .replace("{address}", StrUtil.isEmpty(company.getAddress()) ? "" : company.getAddress())
                        .replace("{director}", StrUtil.isEmpty(company.getDirector()) ? "" : company.getDirector())
                        .replace("{phone}", StrUtil.isEmpty(company.getPhone()) ? "" : company.getPhone())
                        .replace("{signDate}", new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            } else {
                result = contentStr.toString();
            }
        } else {
            result = contractManageService.getHtmlContentByType(businessType, contractType, companyId);
        }

        result = getHtmlContent(companyType, result);

        resultDTO.setHtml(result);
        List<String> fields = contractManageService.getFields(new ServiceDetail());
        resultDTO.setFields(fields);
        return resultDTO;
    }

    /**
     * 在合同中，填写乙方公司信息
     *
     * @param companyType
     * @param result
     * @return
     */
    public String getHtmlContent(Integer companyType, String result) {
        List<InsideInfo> infoList = YamlUtils.read(List.class, "company");
        if (companyType == null || companyType == 1) {
            result = result.replace("{city}", "杭州");
            InsideInfo hzInfo = infoList.get(0);
            result = getInsideInfo(result, hzInfo);
        } else {
            result = result.replace("{city}", "台州");
            InsideInfo zhInfo = infoList.get(1);
            result = getInsideInfo(result, zhInfo);
        }
        return result;
    }

    private String getInsideInfo(String result, InsideInfo insideInfo) {
        result = result.replace("{wt_companyName}", insideInfo.getCompanyName())
                .replace("{wt_address}", insideInfo.getAddress()).replace("{wt_manager}", insideInfo.getManager())
                .replace("{wt_mobile}", insideInfo.getMobile()).replace("{taxNo}", insideInfo.getTaxNo())
                .replace("{bank}", insideInfo.getBank()).replace("{bankNo}", insideInfo.getBankNo())
                .replace("{accountNo}", insideInfo.getAccountNo());
        return result;
    }

    /**
     * 获取合同模板内容
     *
     * @param businessType
     * @param contractType
     * @return
     */
    @Override
    public String getContractInfo(Integer businessType, String contractType) {
        Content content = contentMapper.getContentByType(businessType, contractType);
        String result = content.getContent();
        if (businessType.equals(1)) {
            if ("8".equals(contractType)) {
                result = result + "</html>";
            } else if ("9".equals(contractType)) {
                result = "<html>" + result;
            } else {
                result = "<html>" + result + "</html>";
            }
        }
        return result;
    }

    /**
     * 编辑合同模板内容
     *
     * @param content
     */
    @Override
    public void updateContract(Content content) {
        if (CharSequenceUtil.isEmpty(content.getContent())) {
            throw new BusinessException("合同模板内容不能为空");
        }
        String result = content.getContent();

        if (CharSequenceUtil.isEmpty(content.getContractType())) {
            throw new BusinessException("合同类型不能为空");
        }
        String contractType = content.getContractType();

        if (content.getBusinessType() == null) {
            throw new BusinessException("业务类型不能为空");
        } else {
            if (content.getBusinessType().equals(1)) {
                if ("8".equals(contractType)) {
                    result = result.replace("</html>", "");
                } else if ("9".equals(contractType)) {
                    result = result.replace("<html>", "");
                } else {
                    result = result.replace("<html>", "").replace("</html>", "");
                }
            }
        }
        content.setContent(result);
        contentMapper.updateContent(content);
    }
}
