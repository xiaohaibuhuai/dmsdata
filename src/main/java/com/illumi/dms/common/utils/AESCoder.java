package com.illumi.dms.common.utils;


import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
 
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
/**
 * 
 * @author sniper
 *
 */
public class AESCoder {
 
    /**
     * 迷药算法
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 加密/揭秘算法 / 工作模式 / 填充方式 java7 支持 PCKCS5OADDING
     */
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
 
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
 
    /**
     * 转换密钥
     * 
     * @param key
     *            二进制迷药
     * @return
     */
    private static Key toKey(byte[] key) {
        // 实例化aes 迷药材料
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }
 
    /**
     * 解密
     * 
     * @param data
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] decrypt(byte[] data, byte[] key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 还原密钥
        Key k = toKey(key);
        /**
         * 实例化 使用pkcs7padding 填充方式 安如下方式实现 cipher.getInstance(CIPHER_ALGORITHM,
         * "BC");
         */
 
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化,解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);
        // 执行操作
        return cipher.doFinal(data);
    }
 
    /**
     * 加密
     * 
     * @param data
     *            代加密的数据
     * @param key
     *            密钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static byte[] encrypt(byte[] data, byte[] key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Key k = toKey(key);
        // Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }
 
    /**
     * 生成密钥
     * 
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] initKey() throws NoSuchAlgorithmException {
        // 实例化
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        // aes要求密钥 长度 128位,192位,或者256
        kg.init(256);
        // 生成密钥
        SecretKey secretKey = kg.generateKey();
        // 获取密钥的二进制编码形式
        return secretKey.getEncoded();
    }
 
    /**
     * 初始化密钥,得到密钥的字符串形式
     * 
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public static String initKeyString() throws NoSuchAlgorithmException {
        return Base64.encodeBase64String(initKey());
    }
 
    /**
     * 返回密钥
     * 
     * @param key
     * @return
     */
    public static byte[] getKey(String key) {
        return Base64.decodeBase64(key);
    }
 
    /**
     * 摘要处理
     * 
     * @param data
     * @return
     */
    public static String shaHex(byte[] data) {
        return DigestUtils.md5Hex(data);
    }
 
    /**
     * 验证
     * 
     * @param data
     * @param messageDigest
     * @return
     */
    public static boolean validate(byte[] data, String messageDigest) {
        return messageDigest.equals(shaHex(data));
    }
 
    public static void main(String[] args) throws NoSuchAlgorithmException,
            InvalidKeyException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {
        String inputStr = "AES";
        byte[] inputData = inputStr.getBytes();
 
        System.out.println("原文:\t" + inputStr);
        byte[] key = AESCoder.initKey();
        System.out.println("密钥:\t" + Base64.encodeBase64String(key));
        // 加密
        inputData = AESCoder.encrypt(inputData, key);
        System.out.println("加密后:\t" + Base64.encodeBase64String(inputData));
        // 解密
        byte[] outputData = AESCoder.decrypt(inputData, key);
        System.out.println("解密后:\t" + new String(outputData));
    }
}
