package com.ugdsec.encrty.file;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author liuqi
 * @version v1.0
 * @className zip
 * @description
 * @date 2024/5/8 14:28
 **/
public class ZipChange {

    public static void zipDirectory(String directoryPath, String zipFilePath) {
        Path sourcePath = Paths.get(directoryPath);
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            Files.walk(sourcePath)
                    .filter(path -> !isCompressedFile(path))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try (InputStream is = Files.newInputStream(path);
                             BufferedInputStream bis = new BufferedInputStream(is)) {
                            String fileName = sourcePath.relativize(path).toString();
                            ZipEntry zipEntry = new ZipEntry(fileName);
                            zos.putNextEntry(zipEntry);

                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = bis.read(buffer)) != -1) {
                                zos.write(buffer, 0, bytesRead);
                            }
                            zos.closeEntry();
                        } catch (IOException e) {
                            System.out.println("Error adding file to zip: " + path + " - " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.out.println("Error creating zip file: " + e.getMessage());
        }
    }

    private static boolean isCompressedFile(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        return fileName.endsWith(".zip") || fileName.endsWith(".tar.gz");
    }
}
