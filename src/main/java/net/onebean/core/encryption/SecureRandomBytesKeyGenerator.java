package net.onebean.core.encryption;


import java.security.SecureRandom;

/**
 * 随机盐生成工具
 * @author World
 */
public class SecureRandomBytesKeyGenerator {
    private final SecureRandom random;
    private final int keyLength;
    private static final int DEFAULT_KEY_LENGTH = 8;

    public SecureRandomBytesKeyGenerator() {
        this(8);
    }

    public SecureRandomBytesKeyGenerator(int keyLength) {
        this.random = new SecureRandom();
        this.keyLength = keyLength;
    }

    public int getKeyLength() {
        return this.keyLength;
    }

    public byte[] generateKey() {
        byte[] bytes = new byte[this.keyLength];
        this.random.nextBytes(bytes);
        return bytes;
    }
}
