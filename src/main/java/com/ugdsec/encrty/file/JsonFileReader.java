package com.ugdsec.encrty.file;

/**
 * @className ewewe
 * @description
 * @author liuqi
 * @date 2024/5/7 10:27
 * @version v1.0
 **/

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JsonFileReader {

    public static Map<String, String> readJsonFiles(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }

        Map<String, String> jsonContents = new HashMap<>();
        File[] jsonFiles = directory.listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles != null) {
            for (File file : jsonFiles) {
                Map<String, String> fileContent = readJsonFile(file.toPath());
                jsonContents.putAll(fileContent);
            }
        }
        return jsonContents;
    }

    public static Map<String, String> readJsonFile(Path filePath) {
        try {
            String jsonContent = new String(Files.readAllBytes(filePath));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonContent);
            return objectMapper.convertValue(jsonNode, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + filePath, e);
        }
    }

    public static void writeJsonFile(String filePath, Map<String, String> data) {
        try {
            Path path = Paths.get(filePath);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(path.toFile(), data);
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON file: " + filePath, e);
        }
    }
}
