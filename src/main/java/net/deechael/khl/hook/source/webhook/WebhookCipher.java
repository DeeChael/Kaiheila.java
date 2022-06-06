package net.deechael.khl.hook.source.webhook;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class WebhookCipher {

    /**
     * AES-256-CBC 密钥长度要求
     */
    private static final int SECRET_KEY_LENGTH = 32;
    private static final int INIT_VECTOR_LENGTH = 16;

    /**
     * 通过AES-128-CBC算法加密文本
     * <p>
     * 通过 PKCS5 方式进行填充内容，不足长度的密钥{@code secretKey}将通过后面补 0x00 达到长度。
     * </p>
     *
     * @param secretKey Encrypt Key 密钥
     * @param plainText 需要加密的原文
     * @return 加密结果
     * @throws KeyException 密钥格式错误
     * @throws NoSuchPaddingException Java 发行版本（或版本）不支持 AES-128-CBC AES/CBC/PKCS5Padding 解密算法
     * @throws NoSuchAlgorithmException Java 发行版本（或版本）不支持 AES-128-CBC AES/CBC/PKCS5Padding 解密算法
     * @throws InvalidAlgorithmParameterException 算法参数有误
     * @throws IllegalBlockSizeException AES-CBC 错误数据填充
     * @throws BadPaddingException AES-CBC 错误数据填充
     */
    public static String encrypt(String secretKey, byte[] plainText) throws KeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] byteSecretKey = setValidSecretKeyLength(secretKey);
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[SECRET_KEY_LENGTH];
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        SecretKeySpec secretKeySpec = new SecretKeySpec(byteSecretKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(plainText);
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
        byteBuffer.put(iv);
        byteBuffer.put(encrypted);
        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    /**
     * 通过AES-128-CBC算法解密文本
     * <p>
     * 通过 PKCS5 方式进行填充内容，不足长度的密钥将通过后面补 0x00 达到长度。
     * </p>
     *
     * @param secretKey  Encrypt Key 密钥
     * @param cipherText 需要加密的原文
     * @return 解密结果
     * @throws KeyException 密钥格式错误
     * @throws NoSuchPaddingException Java 发行版本（或版本）不支持 AES-128-CBC AES/CBC/PKCS5Padding 解密算法
     * @throws NoSuchAlgorithmException Java 发行版本（或版本）不支持 AES-128-CBC AES/CBC/PKCS5Padding 解密算法
     * @throws InvalidAlgorithmParameterException 算法参数有误
     * @throws IllegalBlockSizeException AES-CBC 错误数据填充
     * @throws BadPaddingException AES-CBC 错误数据填充
     */
    public static byte[] decrypt(String secretKey, byte[] cipherText) throws KeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] byteSecretKey = setValidSecretKeyLength(secretKey);
        byte[] sourceEncrypted = Base64.getDecoder().decode(cipherText);
        byte[] dataEncrypted = Base64.getDecoder().decode(Arrays.copyOfRange(sourceEncrypted, INIT_VECTOR_LENGTH, sourceEncrypted.length));
        IvParameterSpec ivParameterSpec = new IvParameterSpec(sourceEncrypted, 0, INIT_VECTOR_LENGTH);
        SecretKeySpec secretKeySpec = new SecretKeySpec(byteSecretKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(dataEncrypted);
    }

    /**
     * 设置正确密钥长度
     *
     * @param key 需检查的密钥
     * @return 正确字节数组
     */
    private static byte[] setValidSecretKeyLength(String key) throws KeyException {
        int sourceKeyLength = key.length();
        if (sourceKeyLength == 0) {
            throw new KeyException("Empty Secret Key");
        } else if (sourceKeyLength <= SECRET_KEY_LENGTH) {
            byte[] result = new byte[SECRET_KEY_LENGTH];
            byte[] source = key.getBytes(StandardCharsets.UTF_8);
            System.arraycopy(source, 0, result, 0, sourceKeyLength);
            return result;
        } else {
            int rv = sourceKeyLength - SECRET_KEY_LENGTH;
            return key.substring(0, key.length() + rv).getBytes(StandardCharsets.UTF_8);
        }
    }
}