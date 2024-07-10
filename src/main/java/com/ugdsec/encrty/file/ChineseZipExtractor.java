package org.example.file;

/**
 * @className ff
 * @description  
 * @author liuqi
 * @date 2024/5/7 10:00
 * @version v1.0
**/

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ChineseZipExtractor {
    public static void extractZipFile(String zipFilePath, String extractToDirectory) {
        try (ZipFile zipFile = new ZipFile(zipFilePath, StandardCharsets.UTF_8)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                File outputFile = new File(extractToDirectory, entryName);

                if (entry.isDirectory()) {
                    if (!outputFile.exists()) {
                        outputFile.mkdirs();
                    }
                } else {
                    File parent = outputFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    try (InputStream inputStream = zipFile.getInputStream(entry);
                         OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error extracting ZIP file: " + zipFilePath, e);
        }
    }

    public static void main(String[] args) {

        String zipFilePath = "path/to/your/中文文件名.zip";
        String outputDirectoryName = new File(zipFilePath).getName().replaceFirst("\\.zip$", "");
        String extractToDirectorys = "path/to/extract/directory";
        String extractToDirectory = Paths.get(extractToDirectorys, outputDirectoryName).toString();
        extractZipFile(zipFilePath, extractToDirectory);
        System.out.println("ZIP file extracted successfully!");
    }
}
