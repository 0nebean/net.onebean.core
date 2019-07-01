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
    private static final String UAG_AUTH_USER_INFO = "UAG-AUTH-USER-INFO";
    private static final String APPID_KEY = "appId";
    private static final Logger logger = LoggerFactory.getLogger(UagUtils.class);

    /**
     * 获取当前登录易开小伙伴ID
     * @return string
     */
    @Deprecated
    public static String getCurrentLoginRsSalesId(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String salesId = request.getHeader(UAG_AUTH_USER_ID);
            if (StringUtils.isEmpty(salesId)){
                return null;
            }
            return salesId;
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
            String salesId = request.getHeader(UAG_AUTH_USER_ID);
            if (StringUtils.isEmpty(salesId)){
                return null;
            }
            return salesId;
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
            String salesId = request.getHeader(APPID_KEY);
            if (StringUtils.isEmpty(salesId)){
                return null;
            }
            return salesId;
        } catch (Exception e) {
            logger.error("get appId from http header failure");
        }
        return null;
    }


}
