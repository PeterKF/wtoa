package com.wtkj.oa.utils;

import com.wtkj.oa.exception.BusinessException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * 将word转换成html
 */
public class WordToHtmlUtils {
    private final static String PATH = "resource/";

    /**
     * docx版本word转换成html
     *
     * @throws IOException
     */
    public static void docxToHtml(String fileName) throws IOException {
        String file = PATH + fileName;
        File f = new File(file);
        if (!f.exists()) {
            f.mkdirs();
        } else {
            if (f.getName().endsWith(".docx") || f.getName().endsWith(".DOCX")) {
                String htmlName = fileName.replace(".docx", ".html").replace(".DOCX", ".html");
                // 1) 加载word文档生成 XWPFDocument对象
                InputStream in = new FileInputStream(f);
                XWPFDocument document = new XWPFDocument(in);

                // 2) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
                File imageFolderFile = new File(PATH);
                XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));
                options.setExtractor(new FileImageExtractor(imageFolderFile));
                options.setIgnoreStylesIfUnused(false);
                options.setFragment(true);

                // 3) 将 XWPFDocument转换成XHTML
                OutputStream out = new FileOutputStream(new File(PATH + htmlName));
                XHTMLConverter.getInstance().convert(document, out, options);
            } else {
                throw new BusinessException("该方法用于生成docx文件");
            }
        }
    }

    /**
     * doc版本word转换成html
     *
     * @throws IOException
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public static void docToHtml(String fileName) throws IOException, TransformerException, ParserConfigurationException {
        String htmlName = fileName.replace(".doc", ".html").replace(".DOC", ".html");
        final String file = PATH + fileName;
        InputStream input = new FileInputStream(new File(file));
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        //解析word文档
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();

        File htmlFile = new File(PATH + htmlName);
        OutputStream outStream = new FileOutputStream(htmlFile);

        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");

        serializer.transform(domSource, streamResult);

        outStream.close();
    }

    public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException {
          WordToHtmlUtils.docToHtml("企业研发费归集-技术服务合同.doc");
    }
}
