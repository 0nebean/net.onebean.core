package net.onebean.util;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 字符集操作工具类
 * @author 0neBean
 */
public class EncodingUtils {
    /**
     * 编码
     * @param encodedText 字符串
     * @return 编码后的字符串
     */
    public static String encoderBase64(String encodedText){
        String result = null;
        result = Base64.getEncoder().encodeToString(encodedText.getBytes(StandardCharsets.UTF_8));
        return result;
    }
    /**
     * 编码
     * @param deEncodedText 字符串
     * @return 编码前的字符串
     */
    public static String decoderBase64(String deEncodedText){
        byte[] base64decodedBytes = Base64.getDecoder().decode(deEncodedText);
        return new String(base64decodedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 合并byte数组
     * @param arrays byte数组
     * @return byte数组
     */
    public static byte[] concatenate(byte[]... arrays) {
        int length = 0;
        byte[][] var2 = arrays;
        int destPos = arrays.length;

        for(int var4 = 0; var4 < destPos; ++var4) {
            byte[] array = var2[var4];
            length += array.length;
        }

        byte[] newArray = new byte[length];
        destPos = 0;
        byte[][] var9 = arrays;
        int var10 = arrays.length;

        for(int var6 = 0; var6 < var10; ++var6) {
            byte[] array = var9[var6];
            System.arraycopy(array, 0, newArray, destPos, array.length);
            destPos += array.length;
        }

        return newArray;
    }

    /**
     * 从指定下标合并byte数组
     * @param array byte数组
     * @param beginIndex 开始下标
     * @param endIndex 结束下表
     * @return byte数组
     */
    public static byte[] subArray(byte[] array, int beginIndex, int endIndex) {
        int length = endIndex - beginIndex;
        byte[] subarray = new byte[length];
        System.arraycopy(array, beginIndex, subarray, 0, length);
        return subarray;
    }

    /**
     * 空构造
     */
    private EncodingUtils() {
    }
}
