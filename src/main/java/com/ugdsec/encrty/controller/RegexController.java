package com.ugdsec.encrty.controller;

import com.ugdsec.encrty.common.core.ResultData;
import com.ugdsec.encrty.controller.domain.EncrtyBO;
import com.ugdsec.encrty.controller.domain.RegexBO;
import com.ugdsec.encrty.service.EncrtyService;
import com.ugdsec.encrty.service.RegexMatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/regex")
@RequiredArgsConstructor
public class RegexController {

    private final RegexMatcher regexMatcher;

    @PostMapping(value = "/regex")
    public ResultData<Boolean> encrtyFile(@RequestBody RegexBO regexBO) throws Exception {
        return ResultData.success(regexMatcher.match(regexBO.getRegex(),regexBO.getInput()));
    }
}