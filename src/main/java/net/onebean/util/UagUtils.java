package net.onebean.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 0neBean
 */
public class UagUtils {

    private static final String UAG_AUTH_USER_ID = "UAG-AUTH-USER-ID";
    private static final String APPID_KEY = "appId";
    private static final String DEVICE_TOKEN_KEY = "UAG-AUTH-DEVICE-TOKEN";
    private static final Logger logger = LoggerFactory.getLogger(UagUtils.class);

    /**
     * 获取当前登录用户ID
     * @return string
     */
    @Deprecated
    public static String getCurrentLoginRsSalesId(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String uagUserId = request.getHeader(UAG_AUTH_USER_ID);
            if (StringUtils.isEmpty(uagUserId)){
                return null;
            }
            return uagUserId;
        } catch (Exception e) {
            logger.error("get RS-SALES-INFO-ID from http header failure");
        }
        return null;
    }

    /**
     * 获取当前登录用户ID
     * @return string
     */
    public static String getCurrentLoginUserId(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String uagUserId = request.getHeader(UAG_AUTH_USER_ID);
            if (StringUtils.isEmpty(uagUserId)){
                return null;
            }
            return uagUserId;
        } catch (Exception e) {
            logger.error("get UAG-AUTH-USER-ID from http header failure");
        }
        return null;
    }



    /**
     * 获取当前AppId
     * @return string
     */
    public static String getCurrentAppId(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String appId = request.getHeader(APPID_KEY);
            if (StringUtils.isEmpty(appId)){
                return null;
            }
            return appId;
        } catch (Exception e) {
            logger.error("get appId from http header failure");
        }
        return null;
    }


    /**
     * 获取当前deviceToken
     * @return string
     */
    public static String getCurrentDeviceToken(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String deviceToken = request.getHeader(DEVICE_TOKEN_KEY);
            if (StringUtils.isEmpty(deviceToken)){
                return null;
            }
            return deviceToken;
        } catch (Exception e) {
            logger.error("get appId from http header failure");
        }
        return null;
    }


}
