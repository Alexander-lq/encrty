package org.example.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ReadConf {
    public static Properties readConfigFile(String configFilePath) {
        Properties properties = new Properties();
        try {
            Path configPath = Paths.get(configFilePath);
            if (Files.exists(configPath) && Files.isRegularFile(configPath)) {
                properties.load(Files.newInputStream(configPath));
            } else {
                throw new IllegalArgumentException("Config file not found or not a regular file: " + configFilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading config file: " + configFilePath, e);
        }
        return properties;
    }

    public static void main(String[] args) {
        String configFilePath = "path/to/your/config.conf";
        Properties config = readConfigFile(configFilePath);

        // 访问配置项
        String userName = config.getProperty("user.name");
        String password = config.getProperty("user.password");

        System.out.println("User Name: " + userName);
        System.out.println("Password: " + password);
    }
}
