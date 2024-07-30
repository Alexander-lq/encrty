package com.ugdsec.encrty.service.impl;

import com.ugdsec.encrty.common.FastJsonUtils;
import com.ugdsec.encrty.common.domain.WarnRulePO;
import com.ugdsec.encrty.common.domain.WarnRuleRedisDTO;
import com.ugdsec.encrty.common.domain.param;
import com.ugdsec.encrty.controller.domain.JSONBO;
import com.ugdsec.encrty.service.JsonExchange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Service;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JsonExchangeImpl implements JsonExchange {

    public Integer count = 0;
    static BasicDataSource dataSource;

    static {
        // 初始化连接池
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mariadb://8.135.237.178:3306/mysql");
        dataSource.setUsername("root");
        dataSource.setPassword("Qwer1234@");
        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(40);
    }

    @Override
    public List<WarnRulePO> json(JSONBO jsonbo) {
        // 数据库连接信息
        String url = "jdbc:postgresql://"+jsonbo.getAddress()+":5432/"+jsonbo.getDatabase() + "?currentSchema="+ jsonbo.getSchema(); // 替换为你的数据库名

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        // 用于存储查询结果的列表
        List<WarnRulePO> warnRulePOS = new ArrayList<>();
        try {
            // 加载PostgreSQL JDBC驱动
            Class.forName("org.postgresql.Driver");
            // 创建连接
            connection = DriverManager.getConnection(url, jsonbo.getUsename(), jsonbo.getPassword());
            System.out.println("成功连接到数据库!");
            // 创建Statement对象
            statement = connection.createStatement();
            // 执行查询
            String sql = "SELECT * FROM "+ jsonbo.getTable(); // 替换为你的表名
            resultSet = statement.executeQuery(sql);

            // 处理结果集
            while (resultSet.next()) {
                WarnRulePO warnRulePO = new WarnRulePO();
                // 假设表中有列名为id和name
                warnRulePO.setId(resultSet.getLong("id"));
                String rule = resultSet.getString("rule");
                WarnRuleRedisDTO object = FastJsonUtils.toObject(rule, WarnRuleRedisDTO.class);
                List<param> match = object.getMatch();
                param param = match.get(0);
                warnRulePO.setRegx(param.getValue());
                warnRulePO.setName(object.getRuleName());
                warnRulePO.setIsBuiltin(resultSet.getString("is_builtin"));
                warnRulePOS.add(warnRulePO);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("找不到数据库驱动: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("数据库连接失败: " + e.getMessage());
        } finally {
            // 确保连接在使用后关闭
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("关闭资源时出错: " + e.getMessage());
            }
        }
        return warnRulePOS;
    }

}

