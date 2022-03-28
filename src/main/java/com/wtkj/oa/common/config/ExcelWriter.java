package com.wtkj.oa.common.config;

import cn.hutool.core.exceptions.DependencyException;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import org.apache.poi.xssf.streaming.SXSSFSheet;

/**
 * @Description excel生成类
 * @Date 2022/3/28 19:55
 * @Author Peter.Chen
 */
public class ExcelWriter extends BigExcelWriter {

    public static ExcelWriter getBigWriter() {
        try {
            return new ExcelWriter();
        } catch (NoClassDefFoundError var1) {
            throw new DependencyException((Throwable) ObjectUtil.defaultIfNull(var1.getCause(), var1),
                    "You need to add dependency of 'poi-ooxml' to your project, and version >= 4.1.2", new Object[0]);
        }
    }


    @Override
    public BigExcelWriter autoSizeColumnAll() {
        final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
        sheet.trackAllColumnsForAutoSizing();
        super.autoSizeColumnAll();
        for (int i = 0; i < sheet.getRow(sheet.getLastRowNum()).getPhysicalNumberOfCells(); i++) {
            // 解决自动设置列宽中文失效的问题
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 27 / 10);
        }
        sheet.untrackAllColumnsForAutoSizing();
        return this;
    }

}
