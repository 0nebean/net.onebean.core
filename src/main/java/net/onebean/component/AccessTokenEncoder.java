package net.onebean.component;

import net.onebean.core.encryption.Digester;
import net.onebean.core.encryption.Hex;
import net.onebean.core.encryption.SecureRandomBytesKeyGenerator;
import net.onebean.core.encryption.Utf8;
import net.onebean.util.ByteHexUtils;
import net.onebean.util.EncodingUtils;
import org.springframework.stereotype.Service;

/**
 * AccessToken 生成 解密工具类 
 * @author World
 */
@Service
public class AccessTokenEncoder{

    private final Digester digester;
    private final SecureRandomBytesKeyGenerator saltGenerator;
    private static final int DEFAULT_ITERATIONS = 1024;

    /**
     * 生成AccessToken
     * @param appId 应用ID
     * @param secret 应用秘钥
     * @param timestamp 时间戳
     * @return string AccessToken
     */
    public String generatorAccessToken(String appId, String secret, String timestamp){
        String raw = new String(ByteHexUtils.btyeArrMerge(appId,secret,timestamp));
        return this.encode(raw);
    }

    /**
     * 校验AccessToken
     * @param appId 应用ID
     * @param secret 应用秘钥
     * @param schoolCode 学校代码
     * @param timestamp 时间戳
     * @param accessToken 令牌
     * @return bool 匹配结果
     */
    public Boolean matchAccessToken(String appId, String secret, String schoolCode, String timestamp,String accessToken){
        String raw = new String(ByteHexUtils.btyeArrMerge(appId,secret,schoolCode,timestamp));
        return this.matches(raw,accessToken);
    }

    /**
     * 生成加密串
     * @param raw 原文
     * @return string 加密串
     */
    private String encode(CharSequence raw) {
        return this.encode(raw, this.saltGenerator.generateKey());
    }

    /**
     * 校验加密串
     * @param raw 原文
     * @param encoded 加密串
     * @return bool 是否匹配
     */
    private boolean matches(CharSequence raw, String encoded) {
        byte[] digested = this.decode(encoded);
        byte[] salt = EncodingUtils.subArray(digested, 0, saltGenerator.getKeyLength());
        return this.matches(digested, this.digest(raw, salt));
    }
    /**
     * 默认构造器
     */
    public AccessTokenEncoder() {
        this("SHA-256");
    }

    /**
     * 构造器
     * @param algorithm 加密算法名
     */
    private AccessTokenEncoder(String algorithm) {
        this.digester = new Digester(algorithm, DEFAULT_ITERATIONS);
        saltGenerator = secureRandom();
    }


    /**
     * 加密
     * @param raw 元数据
     * @param salt 盐
     * @return byte数组
     */
    private byte[] digest(CharSequence raw, byte[] salt) {
        byte[] digest = this.digester.digest(EncodingUtils.concatenate(new byte[][]{salt, Utf8.encode(raw)}));
        return EncodingUtils.concatenate(new byte[][]{salt, digest});
    }

    /**
     * 转码
     * @param raw 元数据
     * @param salt 盐
     * @return byte数组
     */
    private String encode(CharSequence raw, byte[] salt) {
        byte[] digest = this.digest(raw, salt);
        return new String(Hex.encode(digest));
    }

    /**
     * 逆向转码
     * @param encoded 编码的结果
     * @return byte数组
     */
    private byte[] decode(CharSequence encoded) {
        return Hex.decode(encoded);
    }

    /**
     * 匹配byte数组
     * @param expected 预期数组
     * @param actual 实际数组
     * @return boolean 匹配结果
     */
    private boolean matches(byte[] expected, byte[] actual) {
        if (expected.length != actual.length) {
            return false;
        } else {
            int result = 0;

            for(int i = 0; i < expected.length; ++i) {
                result |= expected[i] ^ actual[i];
            }

            return result == 0;
        }
    }

    /**
     * 随机加密盐 装载防范
     * @return SecureRandomBytesKeyGenerator
     */
    private static SecureRandomBytesKeyGenerator secureRandom() {
        return new SecureRandomBytesKeyGenerator();
    }



}
