package com.ugdsec.encrty.service;

import com.ugdsec.encrty.common.domain.WarnRulePO;
import com.ugdsec.encrty.controller.domain.JSONBO;

import java.util.List;

public interface JsonExchange {
    List<WarnRulePO> json(JSONBO jsonbo);
}

