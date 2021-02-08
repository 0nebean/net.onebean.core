package net.onebean.core.encryption;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * utf8字符编码转码工具类
 * @author 0neBean
 */
public class Utf8 {
    private static final Charset CHARSET = Charset.forName("UTF-8");

    public Utf8() {
    }

    /**
     * string 转utf8 bye数组
     * @param string 待转字符串
     * @return byte数组
     */
    public static byte[] encode(CharSequence string) {
        try {
            ByteBuffer bytes = CHARSET.newEncoder().encode(CharBuffer.wrap(string));
            byte[] bytesCopy = new byte[bytes.limit()];
            System.arraycopy(bytes.array(), 0, bytesCopy, 0, bytes.limit());
            return bytesCopy;
        } catch (CharacterCodingException var3) {
            throw new IllegalArgumentException("Encoding failed", var3);
        }
    }

    /**
     * byte按照utf8编码转string
     * @param bytes 待转码数组
     * @return String 字符串
     */
    public static String decode(byte[] bytes) {
        try {
            return CHARSET.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException var2) {
            throw new IllegalArgumentException("Decoding failed", var2);
        }
    }
}
