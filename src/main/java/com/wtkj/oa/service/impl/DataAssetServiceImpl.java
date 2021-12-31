package com.wtkj.oa.service.impl;


import com.wtkj.oa.common.config.SqlConfig;
import com.wtkj.oa.entity.Database;
import com.wtkj.oa.entity.TableColumn;
import com.wtkj.oa.utils.DBPool.DBPoolUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.wtkj.oa.service.impl
 * @Description 数据资产
 * @Date 2021/4/28 14:21
 * @Author Peter.Chen
 */
public class DataAssetServiceImpl {

    /**
     * 获取数据库名称
     *
     * @param database
     * @return
     */
    public List<String> getDBNames(Database database) {
        JdbcTemplate jdbcTemplate = DBPoolUtils.getJdbcTemplate(database);
        List<String> dbNames = new ArrayList<>();
        String sql = "";
        switch (database.getType()) {
            case 1:
                sql = SqlConfig.ORACLE_DB_SQL;
                break;
            case 2:
                sql = SqlConfig.MYSQL_DB_SQL;
                break;
            case 3:
                sql = SqlConfig.SQL_SERVER_DB_SQL;
                break;
            case 4:
                sql = SqlConfig.POSTGRESQL_DB_SQL;
        }
        dbNames = jdbcTemplate.queryForList(sql, String.class);
        return dbNames;
    }

    /**
     * 获取oracle数据库下的所有用户
     *
     * @param database
     * @return
     */
    public List<String> getOracleUsers(Database database) {
        JdbcTemplate jdbcTemplate = DBPoolUtils.getJdbcTemplate(database);
        List<String> users = jdbcTemplate.queryForList(SqlConfig.ORACLE_USER_SQL, String.class);
        return users;
    }

    /**
     * 获取某个数据库下的所有表
     *
     * @param database
     * @return
     */
    public List<String> getTables(Database database) {
        JdbcTemplate jdbcTemplate = DBPoolUtils.getJdbcTemplate(database);
        List<String> tableNames = new ArrayList<>();
        String sql = "";
        switch (database.getType()) {
            case 1:
                sql = String.format(SqlConfig.ORACLE_TABLE_SQL, database.getUser());
                break;
            case 2:
                sql = String.format(SqlConfig.MYSQL_TABLE_SQL, database.getDbName());
                break;
            case 3:
                sql = String.format(SqlConfig.SQL_SERVER_TABLE_SQL, database.getDbName());
                break;
            case 4:
                sql = SqlConfig.POSTGRESQL_TABLE_SQL;
        }
        tableNames = jdbcTemplate.queryForList(sql, String.class);
        return tableNames;
    }

    /**
     * 获取某一张表的列信息
     *
     * @param database
     * @return
     */
    public List<TableColumn> getColumns(Database database) {
        JdbcTemplate jdbcTemplate = DBPoolUtils.getJdbcTemplate(database);
        final List<TableColumn> columns = new ArrayList<>();
        String sql = "";
        switch (database.getType()) {
            case 1:
                sql = String.format(SqlConfig.ORACLE_COLUMN_SQL, database.getUser(), database.getUser(), database.getTableName());
                break;
            case 2:
                sql = String.format(SqlConfig.MYSQL_COLUMN_SQL, database.getTableName());
                break;
            case 3:
                sql = SqlConfig.SQL_SERVER_COLUMN_SQL;
                break;
        }
        jdbcTemplate.query(sql, (RowCallbackHandler) rs -> {
            TableColumn column = new TableColumn();
            column.setColumnName(rs.getString("COLUMN_NAME"));
            column.setColumnType(rs.getString("COLUMN_TYPE"));
            column.setColumnComment(rs.getString("COLUMN_COMMENT"));
            columns.add(column);
        });
        return columns;
    }

    /**
     * 随机输出表中的数据
     *
     * @param database
     * @return
     */
    public List<Map<String, Object>> getResultSet(Database database) {
        JdbcTemplate jdbcTemplate = DBPoolUtils.getJdbcTemplate(database);
        String sql = "";
        switch (database.getType()) {
            case 1:
                sql = String.format(SqlConfig.ORACLE_RANDOM_QUERY_SQL, database.getUser() + "." + database.getTableName(), database.getNumber());
                break;
            case 2:
                sql = String.format(SqlConfig.MYSQL_RANDOM_QUERY_SQL, database.getTableName(), database.getNumber());
                break;
            case 3:
                sql = String.format(SqlConfig.SQL_SERVER_RANDOM_QUERY_SQL, database.getNumber(), database.getTableName());
                break;
        }
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        return mapList;
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.67.128", 6379);

        // 设置认证密码，如果没有可以设置为空
        //  jedis.auth("root");

        // 指定数据库 默认是0
        jedis.select(1);

        // 使用ping命令，测试连接是否成功
        String result = jedis.ping();
        System.out.println(result);
        jedis.set("username", "tom");
        String userName = jedis.get("username");
        System.out.println(userName);
        if (jedis != null) {
            jedis.close();
        }
    }
}

