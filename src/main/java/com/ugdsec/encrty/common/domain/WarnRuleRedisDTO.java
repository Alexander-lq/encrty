package com.ugdsec.encrty.common.domain;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

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
public class WarnRuleRedisDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;


    private String ruleName;


    private String eventType;

    private String logSource;


    private String level;


    private String isEmail;


    private String status;


    private String titleTemplate;


    private String descTemplate;


    private List<param> match;


    private String isBuiltin;


    private String thresholdModel;


    private String addHappen;


    private String happenFrequency;


    private String indexName;

}
