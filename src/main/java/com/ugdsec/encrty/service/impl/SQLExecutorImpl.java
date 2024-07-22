package com.ugdsec.encrty.service.impl;

import com.ugdsec.encrty.service.SQLExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
@RequiredArgsConstructor
@Slf4j
public class SQLExecutorImpl implements SQLExecutor {

    public Integer count = 0;
    static BasicDataSource dataSource;

    static {
        // 初始化连接池
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mariadb://192.168.1.59:3306/mysql");
        dataSource.setUsername("root");
        dataSource.setPassword("Qwer1234@");
        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(40);
    }

    @Override
    public String sql(String input) {
        count++;
        log.info("第{}条，要执行的语句:{}", count, input);
        monitorConnectionPool();
        // 使用 try-with-resources 确保资源被正确关闭
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            // 执行 SQL
            boolean hasResultSet = statement.execute(input);
            if (hasResultSet) {
                try (ResultSet resultSet = statement.getResultSet()) {
                    while (resultSet.next()) {
                        // 处理结果集
                    }
                }
            }

            log.info("第{}条，要执行的语句成功", count);
            return "1";
        } catch (SQLException e) {
            log.error("执行 SQL 语句失败: {}", e.getMessage());
            return "0";
        }
    }

    public void monitorConnectionPool() {
        int activeConnections = dataSource.getNumActive();
        int idleConnections = dataSource.getNumIdle();
        log.info("活跃连接数: {}, 空闲连接数: {}", activeConnections, idleConnections);
    }
}

