package com.ugdsec.encrty.controller.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @className EncrtyBO
 * @description  
 * @author liuqi
 * @date 2024/5/8 18:25
 * @version v1.0
**/
@Data
public class EncrtyBO {

    private String fileDecory;
    private String outDecory;
    private String name;

    private List<String> excludeExtensions;

}
