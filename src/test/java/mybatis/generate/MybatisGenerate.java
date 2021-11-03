package mybatis.generate;

import cn.hutool.core.util.RuntimeUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @PACKAGE_NAME: mybatis.generate
 * @Description 自动生成类
 * @Date 2021/7/5 19:50
 * @Author Peter.Chen
 */
@Slf4j
public class MybatisGenerate {

    /**
     * 数据库连接参数
     */
    private static String driverName = "com.mysql.jdbc.Driver";
    private static DbType dbtype = DbType.MYSQL;
    private static String url = "jdbc:mysql://127.0.0.1:3306/wtdb?useUnicode=true&characeterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
    private static String userName = "root";
    private static String password = "123456";

    /**
     * 项目路径
     */
    private static String packagePath = "com.wtkj.oa";
    private static String moduleName = "test";
    private static String projectPath = new File("").getAbsolutePath();
    private static String classPath = "/src/main/java";

    /**
     * 表
     */
    private static String tablePrefix = "";
    private static String[] tableNames = new String[]{"user_info"};

    /**
     * 全局配置
     */
    private static GlobalConfig globalConfig() {
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + classPath);
        gc.setAuthor("Peter.Chen");
        gc.setOpen(false);
        gc.setEnableCache(false);
        gc.setActiveRecord(true);
        gc.setFileOverride(true);
        gc.setBaseColumnList(false);
        gc.setIdType(IdType.ASSIGN_ID);
        gc.setServiceImplName("%sService");
        return gc;
    }

    /**
     * 数据库配置
     */
    private static DataSourceConfig dataSourceConfig() {
        DataSourceConfig config = new DataSourceConfig();
        config.setUrl(url);
        config.setDriverName(driverName);
        config.setUsername(userName);
        config.setPassword(password);
        config.setDbType(dbtype);
        return config;
    }

    private static PackageConfig packageConfig() {
        PackageConfig config = new PackageConfig();
        config.setModuleName(moduleName).setParent(packagePath).setServiceImpl("service");
        return config;
    }

    public static void main(String[] args) {
        generator().execute();
    }

    private static AutoGenerator generator() {
        AutoGenerator generator = new AutoGenerator();
        generator.setGlobalConfig(globalConfig());
        generator.setDataSource(dataSourceConfig());
        generator.setPackageInfo(packageConfig());

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        generator.setCfg(cfg);
        //模板配置
        generator.setTemplate(new TemplateConfig().setXml(null).setController("/controller.java").setService(null)
                .setServiceImpl("serviceImpl.java").setMapper("/mapper.java"));

        //决策配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityBuilderModel(true);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setEntityColumnConstant(true);
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setControllerMappingHyphenStyle(false);
        strategy.setInclude(tableNames);
        strategy.setTablePrefix(tablePrefix);

        List<TableFill> list = new LinkedList<>();
        TableFill create_time = new TableFill("create_time", FieldFill.INSERT);
        list.add(create_time);
        TableFill update_time = new TableFill("update_time", FieldFill.UPDATE);
        list.add(update_time);
        strategy.setTableFillList(list);
        generator.setStrategy(strategy);

        return generator;
    }
}
