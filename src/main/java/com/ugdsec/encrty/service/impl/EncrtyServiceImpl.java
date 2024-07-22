package com.ugdsec.encrty.service.impl;


import com.ugdsec.encrty.controller.domain.EncrtyBO;
import com.ugdsec.encrty.encty.SM2Utils;
import com.ugdsec.encrty.encty.SM3Utils;
import com.ugdsec.encrty.encty.SM4Utils;
import com.ugdsec.encrty.file.JsonFileReader;
import com.ugdsec.encrty.file.ZipChange;
import com.ugdsec.encrty.service.EncrtyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author liuqian
 * @date 2024-05-06
 * @description 业务层接口实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EncrtyServiceImpl implements EncrtyService {



    @Override
    public boolean upload(EncrtyBO encrtyBO) throws Exception {
        String directoryPath = encrtyBO.getFileDecory();
        String zipFilePath =encrtyBO.getOutDecory() + "/" + encrtyBO.getName() +".zip";
        String outDirectory = encrtyBO.getOutDecory();
//        String extractToDirectoryencrypt = "C:\\Users\\liuqi\\Desktop\\fsdownload\\encrypt\\config.json";
//        String conf = "C:\\Users\\liuqi\\Desktop\\fsdownload\\111.conf";
//        String confencrypt = "C:\\Users\\liuqi\\Desktop\\fsdownload\\encrypt\\111.conf";
//        String qcow2 = "C:\\Users\\liuqi\\Desktop\\fsdownload\\base-111-disk-0.qcow2";
        Map<String, String> jsonContents = JsonFileReader.readJsonFiles(directoryPath);
//        String singer = jsonContents.get("singer");
//        jsonContents.remove("singer");
        Map<String, String> sortedJsonContents = new TreeMap<>(jsonContents);
        sortedJsonContents.remove("singer");
        String hash = SM3Utils.encryptBC(sortedJsonContents.toString().getBytes());

        Path directory = Paths.get(directoryPath);
        String conf = Files.list(directory).filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().endsWith(".conf")).map(Path::getFileName)
                .map(Path::toString).findFirst().orElseThrow(() -> new RuntimeException("文件无效"));
        String sm3Hash = SM3Utils.calculateSM3HashForFile(directory+"/"+conf);
        // 获取qcwo2文件完整名称
        String qcwo2 = Files.list(directory).filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().endsWith(".qcow2")).map(Path::getFileName)
                .map(Path::toString).findFirst().orElseThrow(() -> new RuntimeException("文件无效"));
        String singerHash = hash + sm3Hash + SM3Utils.encryptBC(qcwo2.getBytes());

        String key = "EFB35BEAB1CAC5DF4FA96BD89219E7A830AE3DA2308444B895AEC8D358408774";
        String sign = SM2Utils.sign4Der(key, singerHash);
        sortedJsonContents.put("singer",sign);
        JsonFileReader.writeJsonFile(directoryPath+"/config.json",sortedJsonContents);

        File directorys = new File(directoryPath);
        if (!directorys.exists() || !directorys.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }

        // 注册BouncyCastle作为加密服务提供商
        Security.addProvider(new BouncyCastleProvider());

        File[] files = directorys.listFiles((dir, name) -> {
            for (String extension : encrtyBO.getExcludeExtensions()) {
                if (name.endsWith(extension)) {
                    try {
                        // 获取当前操作系统的名称
                        String osName = System.getProperty("os.name");

                        if (osName.startsWith("Windows")) {

                            // 构建 move 命令
                            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "move",
                                    dir + "\\" + name, outDirectory);
                            processBuilder.inheritIO(); // 将命令的输出/错误直接输出到控制台

                            // 执行命令
                            Process process = processBuilder.start();
                            int exitCode = process.waitFor();

                            if (exitCode == 0) {
                                System.out.println("File moved successfully.");
                            } else {
                                System.err.println("Error moving file. Exit code: " + exitCode);
                            }

                        } else {
                            // 构建 mv 命令
                            ProcessBuilder processBuilder =
                                    new ProcessBuilder("mv", dir + "/" + name, outDirectory);
                            processBuilder.inheritIO(); // 将命令的输出/错误直接输出到控制台
                            // 执行命令
                            Process process = processBuilder.start();
                            int exitCode = process.waitFor();

                            if (exitCode == 0) {
                                System.out.println("File moved successfully.");
                            } else {
                                System.err.println("Error moving file. Exit code: " + exitCode);
                            }
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }
            return true;
        });
        if (files != null) {
            for (File file : files) {
                SM4Utils.encryptFile(directoryPath+"/"+file.getName(),outDirectory+"/"+file.getName(),"1234567890123456");
            }
        }

        ZipChange.zipDirectory(outDirectory, zipFilePath);
        System.out.println("Zip file created successfully!");
        System.out.println("Hello world!");
        return true;
    }

    @Override
    public String SM2encrty(EncrtyBO encrtyBO) throws Exception {
        return SM2Utils.encrypt(encrtyBO.getPublicKey(), encrtyBO.getData());
    }

    @Override
    public String SM2decrypt(EncrtyBO encrtyBO) throws Exception {
        return SM2Utils.decrypt(encrtyBO.getPublicKey(), encrtyBO.getData());
    }
}
