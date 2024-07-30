package com.ugdsec.encrty.common.domain;


import java.io.Serializable;
import java.sql.Timestamp;

import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author Liuqian
 * @since 2022-11-10
 */
@Getter
@Setter
public class WarnRulePO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Object rule;

    private String regx;

    private String name;

    private String delFlag;

    private String createBy;

    private Timestamp createTime;

    private String isBuiltin;


}
