package com.wtkj.oa.common.config;

/**
 * sql语句配置类
 */
public class SqlConfig {
    //oracle查询数据库名
    public static final String ORACLE_DB_SQL = "select name from v$database";

    //oracle查询所有用户
    public static final String ORACLE_USER_SQL = "select username from dba_users";

    //oracle查询某个用户下的所有表
    public static final String ORACLE_TABLE_SQL = "select table_name from dba_tables where owner = '%s'";

    //oracle查询某张表的列信息
    public static final String ORACLE_COLUMN_SQL = "SELECT t.column_name,\n" +
            "       CASE\n" +
            "         WHEN t.data_precision IS NOT NULL AND t.data_scale <> 0 THEN\n" +
            "          t.data_type || '(' || t.data_precision || ',' || t.data_scale || ')'\n" +
            "         WHEN t.data_precision IS NOT NULL AND t.data_scale = 0 THEN\n" +
            "          t.data_type || '(' || t.data_precision || ')'\n" +
            "         WHEN t.data_precision IS NULL AND char_col_decl_length IS NOT NULL THEN\n" +
            "          t.data_type || '(' || t.char_col_decl_length || ')'\n" +
            "         ELSE\n" +
            "          t.data_type\n" +
            "       END AS column_type,\n" +
            "       c.comments AS column_comment\n" +
            "  FROM all_tab_columns  t,\n" +
            "       all_col_comments c\n" +
            " WHERE t.owner = '%s'\n" +
            "   AND c.owner = '%s'\n" +
            "   AND c.table_name = t.table_name\n" +
            "   AND c.column_name = t.column_name\n" +
            "   AND t.table_name = '%s'\n" +
            " ORDER BY t.column_id\n";

    //oracle随机查询数据
    public static final String ORACLE_RANDOM_QUERY_SQL = "select * from (select * from %s order by dbms_random.value) where rownum <= %s";


    //mysql查询数据库名
    public static final String MYSQL_DB_SQL = "select SCHEMA_NAME as dbName from information_schema.SCHEMATA";

    //mysql查询表名
    public static final String MYSQL_TABLE_SQL = "select TABLE_NAME as tableName from information_schema.tables " +
            "where TABLE_SCHEMA = '%s'";

    //mysql查询列信息
    public static final String MYSQL_COLUMN_SQL = "select c.column_name as COLUMN_NAME,c.column_type as COLUMN_TYPE," +
            "c.column_comment as COLUMN_COMMENT " +
            "from information_schema.columns c " +
            "where c.TABLE_NAME = '%s'";

    //mysql随机查询数据
    public static final String MYSQL_RANDOM_QUERY_SQL = "select * from %s order by rand() limit %s";

    //sqlServer查询数据库名
    public static final String SQL_SERVER_DB_SQL = "select name from sys.databases";

    //sqlServer查询数据库中的表名
    public static final String SQL_SERVER_TABLE_SQL = "SELECT name FROM %s.sys.sysobjects where type = 'U'";

    //sqlServer查询数据库中的列信息
    public static final String SQL_SERVER_COLUMN_SQL = "SELECT a.name COLUMN_NAME,\n" +
            "case when  \n" +
            "isnull(COLUMNPROPERTY(a.id,a.name,'Scale'),0)=0  \n" +
            "then b.name + '(' + cast(COLUMNPROPERTY(a.id,a.name,'PRECISION') as varchar) + ')' \n" +
            "else b.name + '(' + cast(COLUMNPROPERTY(a.id,a.name,'PRECISION') as varchar) + ','  \n" +
            "+ cast(isnull(COLUMNPROPERTY(a.id,a.name,'Scale'),0) as varchar) + ')'  \n" +
            "end as COLUMN_TYPE,  \n" +
            "isnull(g.[value],'') as COLUMN_COMMENT\n" +
            "FROM  syscolumns a \n" +
            "left join systypes b on a.xtype=b.xusertype  \n" +
            "inner join sysobjects d on a.id=d.id and d.xtype='U' and d.name<>'dtproperties' \n" +
            "left join syscomments e on a.cdefault=e.id  \n" +
            "left join sys.extended_properties g on a.id=g.major_id AND a.colid=g.minor_id\n" +
            "left join sys.extended_properties f on d.id=f.class and f.minor_id=0\n" +
            "where b.name is not null\n" +
            "and d.name= '%s'\n" +
            "order by a.id,a.colorder";

    //sqlServer随机查询数据
    public static final String SQL_SERVER_RANDOM_QUERY_SQL = "select top %s * from %s order by newid()";

    //PostgreSQL查询数据库名称
    public static final String POSTGRESQL_DB_SQL = "select datname from pg_database";

    //PostgreSQL查询数据库中的表名(所有当前数据库下的表)
    public static final String POSTGRESQL_TABLE_SQL = "select tablename from pg_tables where schemaname = 'public'";
}
