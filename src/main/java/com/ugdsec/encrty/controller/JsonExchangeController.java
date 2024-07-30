package com.ugdsec.encrty.controller;

import com.ugdsec.encrty.common.domain.WarnRulePO;
import com.ugdsec.encrty.controller.domain.JSONBO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.ugdsec.encrty.service.JsonExchange;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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
public class JsonExchangeController {

    private final JsonExchange jsonExchange;

    @PostMapping("/export")
    public void exportExcel(@RequestBody JSONBO jsonbo, HttpServletResponse response) throws IOException {
        List<WarnRulePO> json = jsonExchange.json(jsonbo);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("rule");

        // 创建表头和填充数据...
        // 设置响应头
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // 填充数据

        int rowNum = 1;
        for (WarnRulePO user : json) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getIsBuiltin());
            row.createCell(2).setCellValue(user.getName());
            row.createCell(3).setCellValue(user.getRegx());
        }
        // 写入响应流
        try (OutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            outputStream.flush(); // 确保所有数据都被写入
        } catch (IOException e) {
            e.printStackTrace(); // 处理异常并记录详细信息
        } finally {
            try {
                workbook.close(); // 确保工作簿关闭
            } catch (IOException e) {
                e.printStackTrace(); // 处理关闭时的异常
            }
        }
    }
}
