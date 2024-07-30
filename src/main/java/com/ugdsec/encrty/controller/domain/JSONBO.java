package com.ugdsec.encrty.controller.domain;

import lombok.Data;

/**
 * @className EncrtyBO
 * @description
 * @author liuqi
 * @date 2024/5/8 18:25
 * @version v1.0
**/
@Data
public class JSONBO {

    private String address;
    private String usename;
    private String password;
    private String database;
    private String schema;
    private String table;

}
