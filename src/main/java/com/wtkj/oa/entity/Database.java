package com.wtkj.oa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Database implements Serializable {
    private static final long serialVersionUID = -1148929383876545493L;

    private String no;

    /**
     * 1.oracle 2.mysql 3.sqlServer 4.PostgreSQL
     */
    private Integer type;

    private String ip;

    private String port;

    private String instance;

    private String userName;

    private String password;

    private String sql;

    private String dbName;

    private String user;

    private String tableName;

    private Integer number = 0;

    public Database(Integer type, String ip, String port, String instance, String userName, String password) {
        this.type = type;
        this.ip = ip;
        this.port = port;
        this.instance = instance;
        this.userName = userName;
        this.password = password;
    }
}
