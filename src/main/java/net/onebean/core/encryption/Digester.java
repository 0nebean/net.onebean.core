package net.onebean.core.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 0neBean
 * 加密容器
 */
public class Digester {
    private final String algorithm;
    private final int iterations;

    public Digester(String algorithm, int iterations) {
        createDigest(algorithm);
        this.algorithm = algorithm;
        this.iterations = iterations;
    }

    public byte[] digest(byte[] value) {
        MessageDigest messageDigest = createDigest(this.algorithm);

        for(int i = 0; i < this.iterations; ++i) {
            value = messageDigest.digest(value);
        }

        return value;
    }

    private static MessageDigest createDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException var2) {
            throw new IllegalStateException("No such hashing algorithm", var2);
        }
    }
}
