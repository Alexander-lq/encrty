package com.ugdsec.encrty.encty;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.legacy.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.Arrays;
import java.util.List;


public class SM4Utils {




    /**
     * 国密SM4分组密码算法工具类（对称加密）
     * <p>GB/T 32907-2016 信息安全技术 SM4分组密码算法</p>
     * <p>SM4-ECB-PKCS5Padding</p>
     */

    private static final String ALGORITHM_NAME = "SM4";
    private static final String ALGORITHM_ECB_PKCS5PADDING = "SM4/ECB/PKCS5Padding";

    private static final String ENCODING = "UTF-8";

    /**
     * SM4算法目前只支持128位（即密钥16字节）
     */
    private static final int DEFAULT_KEY_SIZE = 128;

    static {
        // 防止内存中出现多次BouncyCastleProvider的实例
        if (null == Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }


//    /**
//     * 加密，SM4-ECB-PKCS5Padding
//     *
//     * @param data 要加密的明文
//     * @return 加密后的密文
//     * @throws Exception 加密异常
//     */
//    public static byte[] encrypt(byte[] data) throws Exception {
//        return sm4(data, StrUtil.hexToBin(SM4_KEY), Cipher.ENCRYPT_MODE);
//    }
//
//    /**
//     * 解密，SM4-ECB-PKCS5Padding
//     *
//     * @param data 要解密的密文
//     * @return 解密后的明文
//     * @throws Exception 解密异常
//     */
//    public static byte[] decrypt(byte[] data) throws Exception {
//        return sm4(data, StrUtil.hexToBin(SM4_KEY), Cipher.DECRYPT_MODE);
//    }

    /**
     * SM4对称加解密
     *
     * @param paramStr 明文（加密模式）或密文（解密模式）
     * @param hexKey   密钥
//     * @param mode     Cipher.ENCRYPT_MODE - 加密；Cipher.DECRYPT_MODE - 解密
     * @return 密文（加密模式）或明文（解密模式）
     * @throws Exception 加解密异常
     */
    public static byte[] sm4(byte[] srcData, String hexKey, int mode) throws Exception {
        try {
            // 16进制字符串--&gt;byte[]
            byte[] keyData = hexKey.getBytes(StandardCharsets.UTF_8);
            // String--&gt;byte[]
//            byte[] srcData = paramStr.getBytes(ENCODING);
//            byte[] srcData = ByteUtils.fromHexString(paramStr);
            SecretKeySpec sm4Key = new SecretKeySpec(keyData, ALGORITHM_NAME);
            Cipher cipher = Cipher
                    .getInstance(ALGORITHM_ECB_PKCS5PADDING, BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(mode, sm4Key);
            byte[] bytes = cipher.doFinal(srcData);
            return bytes;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }



    public static void decryptFiles(String directoryPath,List<String> excludeExtensions,String KEY) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }

        // 注册BouncyCastle作为加密服务提供商
        Security.addProvider(new BouncyCastleProvider());

        File[] files = directory.listFiles((dir, name) -> {
            for (String extension : excludeExtensions) {
                if (name.endsWith(extension)) {
                    return false;
                }
            }
            return true;
        });
        if (files != null) {
            for (File file : files) {
                String decryptedFilePath = new File("D:\\", file.getName()).getPath();
                decryptFile(file.getPath(), decryptedFilePath,KEY);
            }
        }
    }

    private static void decryptFile(String encryptedFilePath, String decryptedFilePath,String KEY) {
        try {
            // 将密钥转换为SecretKeySpec对象
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "SM4");

            // 创建SM4解密器
            Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // 读取加密文件并解密
            try (FileInputStream fis = new FileInputStream(encryptedFilePath);
                 FileOutputStream fos = new FileOutputStream(decryptedFilePath)) {
                long fileSize = new File(encryptedFilePath).length();
                byte[] buffer = new byte[(int) fileSize];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
                    fos.write(decryptedBytes);
                }
                byte[] finalDecryptedBytes = cipher.doFinal();
                fos.write(finalDecryptedBytes);
                fos.flush();
            }
            // 删除原始的加密文件
            if (new File(encryptedFilePath).delete()) {
                System.out.println("File decrypted and deleted successfully: " + decryptedFilePath);
            } else {
                System.out.println("File decrypted successfully: " + decryptedFilePath);
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error decrypting file: " + encryptedFilePath, e);
        }
    }



    public static void encryptFile(String inputFilePath, String encryptedFilePath, String KEY) {
        try {
            // 将密钥转换为SecretKeySpec对象
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "SM4");

            // 创建SM4加密器
            Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // 读取原始文件并加密
            try (FileInputStream fis = new FileInputStream(inputFilePath);
                 FileOutputStream fos = new FileOutputStream(encryptedFilePath)) {
                long fileSize = new File(inputFilePath).length();
                byte[] buffer = new byte[(int) fileSize];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
                    fos.write(encryptedBytes);
                }
                byte[] finalEncryptedBytes = cipher.doFinal();
                fos.write(finalEncryptedBytes);
                fos.flush();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Error encrypting file: " + inputFilePath, e);
        }
    }


    /**
     * 字符串工具类
     */

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String binToHex(byte[] bytes) {
        return Hex.toHexString(bytes).toUpperCase();
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hex 字节数组
     * @return 十六进制字符串
     */
    public static byte[] hexToBin(String hex) {
        return Hex.decode(hex);
    }

    /**
     * 字节数组转UTF8字符串
     *
     * @param bytes 字节数组
     * @return UTF8字符串
     */
    public static String binToStr(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * UTF8字符串转字节数组
     *
     * @param str UTF8字符串
     * @return 字节数组
     */
    public static byte[] strToBin(String str) {
        return Strings.toUTF8ByteArray(str);
    }
}
