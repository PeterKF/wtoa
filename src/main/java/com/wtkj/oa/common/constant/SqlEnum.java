package com.wtkj.oa.common.constant;

public enum SqlEnum {
    ORACLE_D("", "oracle查询库名"),
    ORACLE_T("", "oracle查询表名"),
    ORACLE_C("", "oracle查询列名"),
    MYSQL_D("", "mysql查询库名"),
    MYSQL_T("", "mysql查询库名"),
    MYSQL_C("", "mysql查询库名"),
    SQL_SERVER_D("", "sqlServer查询库名"),
    SQL_SERVER_T("", "sqlServer查询库名"),
    SQL_SERVER_C("", "sqlServer查询库名");

    SqlEnum(String sql, String desc) {
        this.sql = sql;
        this.desc = desc;
    }

    private String sql;

    private String desc;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
