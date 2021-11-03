package com.wtkj.oa.utils;


import com.wtkj.oa.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import java.io.*;

public class FileUploadUtils {
    private static Logger logger = LoggerFactory.getLogger(FileUploadUtils.class);

    /**
     * 文件转换成二进制数组
     *
     * @param fileName
     * @param path
     * @return
     */
    public static byte[] fileToByte(String fileName, String path) {
        File file = new File(path + fileName);
        if (file.exists()) {
            try {
                InputStream is = new FileInputStream(file);
                byte[] bytes = FileCopyUtils.copyToByteArray(is);
                return bytes;
            } catch (FileNotFoundException e) {
                throw new BusinessException("文件不存在！");
            } catch (IOException e) {
                throw new BusinessException("ERROR:文件没有成功转换成二进制文件，" + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 将二进制转换成文件
     *
     * @param content
     * @param filePath
     */
    public static void byteToFile(byte[] content, String filePath) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream output = null;
        try {
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(content);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            // 获取文件的父路径字符串
            File path = file.getParentFile();
            if (!path.exists()) {
                logger.info("文件夹不存在，创建。path={}", path);
                boolean isCreated = path.mkdirs();
                if (!isCreated) {
                    logger.error("创建文件夹失败，path={}", path);
                }
            }
            fos = new FileOutputStream(file);
            // 实例化OutputString 对象
            output = new BufferedOutputStream(fos);
            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                output.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            output.flush();
        } catch (Exception e) {
            logger.error("输出文件流时抛异常，filePath={}", filePath, e);
        } finally {
            try {
                bis.close();
                fos.close();
                output.close();
            } catch (IOException e0) {
                logger.error("文件处理失败，filePath={}", filePath, e0);
            }
        }
    }
}
