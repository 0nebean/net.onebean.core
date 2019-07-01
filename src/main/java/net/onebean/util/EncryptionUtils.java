package net.onebean.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

/**
 * 加密类
 * @author 0neBean
 */
public class EncryptionUtils {
    /**
     * 产生一个36个字符的UUID
     *
     * @return UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * md5加密
     *
     * @param value 要加密的值
     * @return md5加密后的值
     */
    public static String md5Hex(String value) {
        return DigestUtils.md5Hex(value);
    }


    /**
     * sha256加密
     *
     * @param value 要加密的值
     * @return sha256加密后的值
     */
    public static String sha256Hex(String value) {
        return DigestUtils.sha256Hex(value);
    }

    /**
     * sha1加密
     *
     * @param value 要加密的值
     * @return sha256加密后的值
     */
    public static String sha1(String value) {
        return DigestUtils.sha1Hex(value);
    }


    /**
     * md5加密
     *
     * @param values 要加密的值
     * @return md5加密后的值
     */
    public static String md5Hex(String ...values) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values) {
            stringBuilder.append(value);
        }
        return DigestUtils.md5Hex(stringBuilder.toString());
    }


    /**
     * sha1加密
     *
     * @param values 要加密的值
     * @return sha256加密后的值
     */
    public static String sha1(String ...values) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values) {
            stringBuilder.append(value);
        }
        return DigestUtils.sha1Hex(stringBuilder.toString());
    }


    /**
     * sha256加密
     *
     * @param values 要加密的值
     * @return sha256加密后的值
     */
    public static String sha256Hex(String ...values) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values) {
            stringBuilder.append(value);
        }
        return DigestUtils.sha256Hex(stringBuilder.toString());
    }
}
