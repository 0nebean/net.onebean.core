package net.onebean.core.encryption;

/**
 * 十六进制编码转换工具类
 * @author 0neBean
 */
public class Hex {
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public Hex() {
    }

    /**
     * byte数组转十六进制数组
     * @param bytes 待转码数组
     * @return char数组
     */
    public static char[] encode(byte[] bytes) {
        int nBytes = bytes.length;
        char[] result = new char[2 * nBytes];
        int j = 0;

        for(int i = 0; i < nBytes; ++i) {
            result[j++] = HEX[(240 & bytes[i]) >>> 4];
            result[j++] = HEX[15 & bytes[i]];
        }

        return result;
    }

    /**
     * 逆向转码
     * @param s 待还原字符串
     * @return byte数组
     */
    public static byte[] decode(CharSequence s) {
        int nChars = s.length();
        if (nChars % 2 != 0) {
            throw new IllegalArgumentException("Hex-encoded string must have an even number of characters");
        } else {
            byte[] result = new byte[nChars / 2];

            for(int i = 0; i < nChars; i += 2) {
                int msb = Character.digit(s.charAt(i), 16);
                int lsb = Character.digit(s.charAt(i + 1), 16);
                if (msb < 0 || lsb < 0) {
                    throw new IllegalArgumentException("Detected a Non-hex character at " + (i + 1) + " or " + (i + 2) + " position");
                }

                result[i / 2] = (byte)(msb << 4 | lsb);
            }

            return result;
        }
    }
}
