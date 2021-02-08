package net.onebean.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import java.util.Calendar;

/**
 * @author 0neBean
 */
public class TokenCheckUtils {

    private static Logger logger = Logger.getLogger(TokenCheckUtils.class);
    private final static double SAFE_TIME_LIMIT = 5;

    /**
     * 是否是合法请求accessToken的TimeStamp
     * @param timeStamp 待校验的TimeStamp
     * @return bool
     */
    public static boolean legalTimeStamp4AccToken(String timeStamp){
        return StringUtils.isNotEmpty(timeStamp) && timeStamp.length() == 13 && isFreshTimeStamp(timeStamp);
    }

    /**
     * 是否新鲜的TimeStamp
     * @param timeStamp 待校验的TimeStamp
     * @return bool
     */
    public static boolean isFreshTimeStamp(String timeStamp) {
        logger.info("System.currentTimeMillis() = "+System.currentTimeMillis());
        if (StringUtils.isEmpty(timeStamp))
            return false;
        Calendar calendar = Calendar.getInstance();
        long timeStamp_long = Long.valueOf(timeStamp);
        long now  = calendar.getTime().getTime();
        double s = (Math.abs(now - timeStamp_long)) / (1000 * 60);
        return s <= SAFE_TIME_LIMIT;
    }

    /**
     * 校验sign 是否合法
     * @param sign 签名
     * @param appId 应用ID
     * @param secret 应用秘钥
     * @param customerId 业务用户ID
     * @param tenantId 租户ID
     * @param deviceToken 设备码
     * @param timestamp 时间戳
     * @return bool
     */
    public static boolean checkSignMd5(String sign,String appId,String secret,String customerId,String tenantId,String deviceToken, String timestamp){
        String sb = appId +
                secret +
                customerId +
                tenantId +
                deviceToken +
                timestamp;
        return legalTimeStamp4AccToken(timestamp) && sign.equalsIgnoreCase(DigestUtils.md5Hex(sb));
    }


    /**
     * 生成签名
     * @param appId 应用ID
     * @param secret 应用秘钥
     * @param customerId 业务用户ID
     * @param tenantId 租户ID
     * @param deviceToken 设备码
     * @param timestamp 时间戳
     * @return bool
     */
    public static String sign(String appId,String secret,String customerId,String tenantId,String deviceToken, String timestamp){
        String sb = appId +
                secret +
                customerId +
                tenantId +
                deviceToken +
                timestamp;
        return DigestUtils.md5Hex(sb);
    }
    /**
     * 生成签名
     * @param args 可变参数
     * @return sign
     */
    public static String sign (String... args){
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg);
        }
        return DigestUtils.md5Hex(sb.toString());
    }

    /**
     * 校验签名有效性
     * @param sign 签名
     * @param timestamp 时间戳 仅用作对比 不包含在可变参数内
     * @param args 可变参数
     * @return bool
     */
    public static boolean checkSign(String sign,String timestamp,String... args){

        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg);
        }
        logger.info("DigestUtils.md5Hex(sb.toString()) = "+DigestUtils.md5Hex(sb.toString()));
        logger.info("System.currentTimeMillis() = "+System.currentTimeMillis());
        if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(timestamp)){
            return false;
        }


        return legalTimeStamp4AccToken(timestamp) && sign.equalsIgnoreCase(DigestUtils.md5Hex(sb.toString()));
    }

}
