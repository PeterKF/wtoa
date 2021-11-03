package com.wtkj.oa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TableColumn {
    private String no;

    private String tableName;

    private String tableComment;

    private String columnName;

    private String columnType;

    private String columnComment;

    private List<TableColumn> columns;
}
