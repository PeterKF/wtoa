package com.wtkj.oa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DataAsset {
    private String no;

    private Integer type;

    private String dbName;

    private List<String> users;

    private List<TableColumn> tableColumns;
}
