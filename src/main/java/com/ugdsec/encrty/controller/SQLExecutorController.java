package com.ugdsec.encrty.controller;

import com.ugdsec.encrty.common.core.ResultData;
import com.ugdsec.encrty.controller.domain.RegexBO;
import com.ugdsec.encrty.controller.domain.SQLBO;
import com.ugdsec.encrty.service.RegexMatcher;
import com.ugdsec.encrty.service.SQLExecutor;
import com.ugdsec.encrty.service.impl.SQLExecutorImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className encrtyController
 * @description
 * @author liuqi
 * @date 2024/5/8 18:12
 * @version v1.0
**/
@RestController
@RequestMapping("/sql")
@RequiredArgsConstructor
public class SQLExecutorController {

    private final SQLExecutor sqlExecutor;
    private int count = 0;

    @PostMapping(value = "/sqlExecutor")
    public ResultData<String> sqlExecutor(SQLBO sqlbo) throws Exception {
        count++;
        System.out.println("计数："+count);
        return ResultData.success(sqlExecutor.sql(sqlbo.getInput()));
    }
}
