package com.wtkj.oa.utils;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.wtkj.oa.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Yaml文件读写静态方法
 */
public class YamlUtils {
    private final static Logger logger = LoggerFactory.getLogger(YamlUtils.class);
    private final static String FILE_PATH = "resource/info/";
    private final static String YAML_SUFFIX = ".yml";

    /**
     * 从.yml文件读取数据
     *
     * @param classType 读出的实例类型
     * @param fileName  文件名称
     * @param <T>
     * @return
     */
    public static <T> T read(Class<T> classType, String fileName) {
        return readWithOutFilepath(classType, FILE_PATH + fileName);
    }

    public static <T> T readWithOutFilepath(Class<T> classType, String fileName) {
        try {
            //读取yml文件
            YamlReader reader = new YamlReader(new FileReader(fileName + YAML_SUFFIX));
            T t = reader.read(classType);
            return t;
        } catch (FileNotFoundException e) {
            logger.error("YamlUtils read file : " + fileName + YAML_SUFFIX + " failed ! file is not Exist");
            return null;
        } catch (Exception e) {
            logger.error("YamlUtils read file : " + fileName + YAML_SUFFIX + " failed ! error : " + e);
            throw new BusinessException("读取Yaml数据文件【" + fileName + YAML_SUFFIX + "】失败");
        }
    }

    public static <T> T readWithOutSuffix(Class<T> classType, String fileName) {
        try {
            //读取yml文件
            YamlReader reader = new YamlReader(new FileReader(FILE_PATH + fileName));
            T t = reader.read(classType);
            reader.close();
            return t;
        } catch (FileNotFoundException e) {
            logger.error("YamlUtils read file : " + FILE_PATH + fileName + " failed ! file is not Exist");
            return null;
        } catch (Exception e) {
            logger.error("YamlUtils readWithOutSuffix file : " + FILE_PATH + fileName + " failed ! error : " + e);
            throw new BusinessException("读取Yaml数据文件【" + FILE_PATH + fileName + "】失败");
        }
    }

    public static <T> T readWithOutFilepathORSuffix(Class<T> classType, String fileName) {
        try {
            //读取yml文件
            YamlReader reader = new YamlReader(new FileReader(fileName));
            T t = reader.read(classType);
            reader.close();
            return t;
        } catch (FileNotFoundException e) {
            logger.error("YamlUtils read file : " + fileName + " failed ! file is not Exist");
            return null;
        } catch (Exception e) {
            logger.error("YamlUtils readWithOutSuffix file : " + fileName + " failed ! error : " + e);
            throw new BusinessException("读取Yaml数据文件【" + fileName + "】失败");
        }
    }


    /**
     * 将一个实例写入一个.yml文件
     *
     * @param obj      入参实例
     * @param fileName 文件名称
     */
    public static void write(Object obj, String fileName) {
        try {
            YamlWriter writer = new YamlWriter(new FileWriter(FILE_PATH + fileName + YAML_SUFFIX));
            writer.write(obj);
            writer.close();
        } catch (Exception e) {
            logger.error("YamlUtils write file : " + FILE_PATH + fileName + " failed ! The className is : "
                    + obj.getClass().getName() + ", error: {}", e);
            throw new BusinessException("写入Yaml数据文件【" + FILE_PATH + fileName + YAML_SUFFIX + "】失败");
        }
    }


    public static void writeNoFilePathORSuffix(Object obj, String fileName) {
        try {
            YamlWriter writer = new YamlWriter(new FileWriter(fileName));
            writer.write(obj);
            writer.close();
        } catch (Exception e) {
            logger.error("YamlUtils write file : " + fileName + " failed ! The className is : "
                    + obj.getClass().getName() + ", error : " + e);
            throw new BusinessException("写入Yaml数据文件【" + fileName + YAML_SUFFIX + "】失败");
        }
    }


    public static void writeNoFilePath(Object obj, String fileName) {
        try {
            YamlWriter writer = new YamlWriter(new FileWriter(fileName + YAML_SUFFIX));
            writer.write(obj);
            writer.close();
        } catch (Exception e) {
            logger.error("YamlUtils write file : " + fileName + " failed ! The className is : "
                    + obj.getClass().getName() + ", error : " + e);
            throw new BusinessException("写入Yaml数据文件【" + fileName + YAML_SUFFIX + "】失败");
        }
    }


    public static void deleteFileNoFilePath(String fileName) {
        File file = new File(fileName);

        if (file.exists()) {
            if (file.isFile()) {
                if (file.delete()) {
                    logger.info("delete file sussess, fileName: " + fileName);
                } else {
                    throw new BusinessException("删除操作失败, filePath: " + fileName);
                }
            }
        } else {
            logger.info("YamlUtils delete file : " + fileName + " failed ! file is not Exist");
        }
    }


    public static void deleteFile(String fileName) {
        deleteFileNoFilePath(FILE_PATH + fileName + YAML_SUFFIX);
    }

    public static void batchDelete(String filePath, String namePerfix) {
        File file = new File(FILE_PATH + filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().startsWith(namePerfix)) {
                    deleteFileNoFilePath(files[i].getAbsolutePath());
                }
            }
        }
    }

    public static String getFilePath() {
        return FILE_PATH;
    }
}
