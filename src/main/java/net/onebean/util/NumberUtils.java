package net.onebean.util;

import java.math.BigDecimal;

/**
 * 数字工具类
 */
public class NumberUtils {

    /**
     * a 小于 b
     * @param a a
     * @param b b
     * @return bool
     */
    public static Boolean aXYb(BigDecimal a,BigDecimal b){
        return a.compareTo(b) < 0;
    }
    /**
     * a 等于 b
     * @param a a
     * @param b b
     * @return bool
     */
    public static Boolean aEQb(BigDecimal a,BigDecimal b){
        return a.compareTo(b) == 0;
    }
    /**
     * a 大于 b
     * @param a a
     * @param b b
     * @return bool
     */
    public static Boolean aDYb(BigDecimal a,BigDecimal b){
        return a.compareTo(b) > 0;
    }
    /**
     * a 大于等于 b
     * @param a a
     * @param b b
     * @return bool
     */
    public static Boolean aDYEQb(BigDecimal a,BigDecimal b){
        return a.compareTo(b) > -1;
    }
    /**
     * a 小于等于 b
     * @param a a
     * @param b b
     * @return bool
     */
    public static Boolean aXYEQb(BigDecimal a,BigDecimal b){
        return a.compareTo(b) < 1;
    }
    /**
     * 是否是负数
     * @param val 值
     * @return bool
     */
    public static Boolean isMinus(BigDecimal val){
        return val.compareTo(BigDecimal.ZERO) < 0;
    }
    /**
     * 是否是负数
     * @param val 值
     * @return bool
     */
    public static Boolean isMinus(Integer val){
        return val < 0;
    }
    /**
     * 是否是负数
     * @param val 值
     * @return bool
     */
    public static Boolean isMinus(Long val){
        return val < 0;
    }
    /**
     * a 是否足够 扣除 b
     * @param a a
     * @param b b
     * @return bool
     */
    public static Boolean aEnoughSubtractionB(BigDecimal a,BigDecimal b){
        return !isMinus(a.subtract(b));
    }
}
