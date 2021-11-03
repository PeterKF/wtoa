package com.wtkj.oa.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderUtils {
    private final static Logger LOG = LoggerFactory.getLogger(ExcelReaderUtils.class);

    /**
     * 根据fileType不同读取excel文件
     *
     * @param path
     *
     * @param path
     * @throws IOException
     */
    public static List<List<String>> readExcel(String path, int sheetNum) {
        String fileType = path.substring(path.lastIndexOf(".") + 1);
        List<List<String>> lists = new ArrayList<List<String>>();
        //读取excel文件
        InputStream is = null;
        try {
            is = new FileInputStream(path);
            //获取工作薄
            Workbook wb = null;
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                return null;
            }

            //读取工作页sheet
            Sheet sheet = wb.getSheetAt(sheetNum);
            for (Row row : sheet) {
                List<String> list = new ArrayList<>();
                for (Cell cell : row) {
                    //根据不同类型转化成字符串
                    cell.setCellType(CellType.STRING);
                    list.add(cell.getStringCellValue());
                }
                lists.add(list);
            }
        } catch (IOException e) {
            LOG.error("error: " + e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                LOG.error("error: " + e);
            }
        }
        return lists;
    }
}
