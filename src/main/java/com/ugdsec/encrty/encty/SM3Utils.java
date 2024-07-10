package com.ugdsec.encrty.encty;


import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Base64;

/**
 * 国密SM3摘要算法实现工具.
 * 用于替换MD5、SHA算法
 */
public class SM3Utils {

    private static final String ENCODING = "UTF-8";

    // 拓展算法提供方，加载BC库.
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * SM3摘要算法逻辑
     *
     * @param srcData 待加密byte[]
     * @return
     */
    public static byte[] sm3(byte[] srcData) {
        SM3Digest sm3Digest = new SM3Digest();
        sm3Digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[sm3Digest.getDigestSize()];
        sm3Digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * 摘要算法加密
     *
     * @param paramStr 待加密byte[]
     * @return
     */
    public static String encrypt(String paramStr) throws Exception{
        byte[] srcData = paramStr.getBytes(ENCODING);
        byte[] hash = sm3(srcData);
        String hexString = Hex.toHexString(hash);
        return hexString;
    }

    /**
     * 指定密钥进行加密.
     *
     * @param key     密钥
     * @param srcData 待加密byte[]
     * @return
     */
    private static byte[] hmacSm3(byte[] key, byte[] srcData) {
        KeyParameter keyParameter = new KeyParameter(key);
        SM3Digest digest = new SM3Digest();
        HMac mac = new HMac(digest);
        mac.init(keyParameter);
        mac.update(srcData, 0, srcData.length);
        byte[] hash = new byte[mac.getMacSize()];
        mac.doFinal(hash, 0);
        return hash;
    }

    /**
     * 指定密钥进行加密.
     *
     * @param key     密钥
     * @param srcData 待加密byte[]
     * @return
     */
    public static String encrypt(byte[] key, byte[] srcData) {
        byte[] hash = hmacSm3(key, srcData);
        String hexString = Hex.toHexString(hash);
        return hexString;
    }

    /**
     * 基于BC库的摘要算法加密
     *
     * @param srcData 待加密byte[]
     * @return
     * @throws Exception
     */
    private static byte[] sm3bc(byte[] srcData) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SM3", "BC");
        byte[] digest = messageDigest.digest(srcData);
        return digest;
    }

    /**
     * 基于BC库的摘要算法加密
     *
     * @param srcData 待加密byte[]
     * @return
     * @throws Exception
     */
    public static String encryptBC(byte[] srcData) throws Exception {
        byte[] hash = sm3bc(srcData);
        String hexString = Hex.toHexString(hash);
        return hexString;
    }

    public static String calculateSM3HashForFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);
            byte[] hashBytes = calculateSM3Digest(fileBytes);
            return Hex.toHexString(hashBytes);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }

    private static byte[] calculateSM3Digest(byte[] content) {
        SM3Digest digest = new SM3Digest();
        digest.update(content, 0, content.length);
        byte[] hashBytes = new byte[digest.getDigestSize()];
        digest.doFinal(hashBytes, 0);
        return hashBytes;
    }


}
