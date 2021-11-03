package com.wtkj.oa.utils;

import com.wtkj.oa.exception.BusinessException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;

/**
 * 读取word内容
 */
public class ReadWordUtils {

    /**
     * 读取doc文件内容
     *
     * @param pathName
     * @return
     */
    public static String readDocFile(String pathName) {
        File file = new File(pathName);
        String str = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            HWPFDocument doc = new HWPFDocument(fis);
            Range rang = doc.getRange();
            str = rang.text();
            fis.close();
        } catch (Exception e) {
            throw new BusinessException("word文档读取失败，error:" + e);
        }
        return str;
    }

    /**
     * 读取docx文件内容
     *
     * @param pathName
     * @return
     */
    public static String readDocxFile(String pathName) {
        File file = new File(pathName);
        String str = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            str = extractor.getText();
            fis.close();
        } catch (Exception e) {
            throw new BusinessException("word文档读取失败，error:" + e.getMessage());
        }
        return str;
    }
}
