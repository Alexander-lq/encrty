package com.ugdsec.encrty.controller;

import com.ugdsec.encrty.common.core.ResultData;
import com.ugdsec.encrty.controller.domain.EncrtyBO;
import com.ugdsec.encrty.service.EncrtyService;
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
@RequestMapping("/encrty")
@RequiredArgsConstructor
public class encrtyController {

    private final EncrtyService encrtyService;

    @PostMapping(value = "/upload")
    public ResultData<Boolean> encrtyFile(@RequestBody EncrtyBO encrtyBO) throws Exception {
        return ResultData.success(encrtyService.upload(encrtyBO));
    }

    @PostMapping(value = "/SM2encrty")
    public ResultData<String> encrtySM2(@RequestBody EncrtyBO encrtyBO) throws Exception {
        return ResultData.success(encrtyService.SM2encrty(encrtyBO));
    }

    @PostMapping(value = "/SM2decrypt")
    public ResultData<String> decryptSM2(@RequestBody EncrtyBO encrtyBO) throws Exception {
        return ResultData.success(encrtyService.SM2decrypt(encrtyBO));
    }
}
