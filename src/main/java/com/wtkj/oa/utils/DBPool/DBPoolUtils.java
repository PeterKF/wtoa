package com.wtkj.oa.utils.DBPool;

import com.wtkj.oa.entity.Database;
import com.wtkj.oa.exception.BusinessException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DBPoolUtils {
    private DBPoolUtils() {
    }

    private static String oracleDriver = "oracle.jdbc.driver.OracleDriver";
    private static String mysqlDriver = "com.mysql.jdbc.Driver";
    private static String sqlServerDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String postgreSQLDriver = "org.postgresql.Driver";


    /**
     * 获取数据库连接
     *
     * @param database
     * @return
     */
    public static JdbcTemplate getJdbcTemplate(Database database) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        String driver = "";
        String url = "";
        switch (database.getType()) {
            case 1:
                url = "jdbc:oracle:thin:@//" + database.getIp() + ":" + database.getPort() + "/" + database.getInstance();
                driver = oracleDriver;
                break;
            case 2:
                url = "jdbc:mysql://" + database.getIp() + ":" + database.getPort() + "/" + database.getDbName()
                        + "?useUnicode=true&characeterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
                driver = mysqlDriver;
                break;
            case 3:
                url = "jdbc:sqlserver://" + database.getIp() + ":" + database.getPort() + ";DatabaseName=" + database.getDbName();
                driver = sqlServerDriver;
                break;
            case 4:
                url = "jdbc：postgresql://" + database.getIp() + ":" + database.getPort() + "/" + database.getDbName();
                driver = postgreSQLDriver;
                break;
            default:
                throw new BusinessException("该数据库类型目前不支持！");
        }
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(database.getUserName());
        dataSource.setPassword(database.getPassword());
        return new JdbcTemplate(dataSource);
    }

    /**
     * 执行sql（判断sql是否可以运行）
     *
     * @param database
     */
    public static void executeSql(Database database) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(database);
        try {
            jdbcTemplate.execute(database.getSql());
        } catch (DataAccessException e) {
            throw new BusinessException("sql语句执行失败，e" + e.getMessage());
        }
    }
}
