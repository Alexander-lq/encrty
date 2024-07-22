package com.ugdsec.encrty.service;

import com.ugdsec.encrty.controller.domain.EncrtyBO;

/**
 * @author liuqian
 * @date 2024-05-06
 * @description  业务层接口
 */
public interface EncrtyService{


    boolean upload(EncrtyBO encrtyBO) throws Exception;

    String SM2encrty(EncrtyBO encrtyBO) throws Exception;

    String SM2decrypt(EncrtyBO encrtyBO) throws Exception;

}
