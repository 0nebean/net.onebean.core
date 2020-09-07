package net.onebean.util;

import java.util.ArrayList;
import java.util.List;

/**
 * byte数组操作工具
 * @author World
 */
public class ByteHexUtils {

    /**
     * 合并byte数组
     * @param args 字符串
     * @return byte[] 返回类型
     */
    public static byte[] btyeArrMerge(String...args) {
        List<byte[]> srcArrays =  new ArrayList<>();
        for (String str : args) {
            srcArrays.add(str.getBytes());
        }
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
            destLen += srcArray.length;
        }
        return destArray;
    }

    /**
     * 获取数据中偶数下标的数组
     * @param arr byte数组
     * @return byte数组
     */
    public static byte[] getEvenNumberByteArray(byte[] arr){
        byte[] res = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if(i%2==0){
                res[i] = arr[i];
            }
        }
        return res;
    }
}
