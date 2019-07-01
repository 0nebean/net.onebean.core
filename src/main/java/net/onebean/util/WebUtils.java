package net.onebean.util;

import javax.servlet.http.HttpServletRequest;

/**
 * web 工具类
 * @author 0neBean
 */
public class WebUtils {

    private static final String TYPE_OF_ANDROID = "0";
    private static final String TYPE_OF_IOS = "1";
    private static final String TYPE_OF_MICRO_MESSAGE = "2";
    private static final String TYPE_OF_OTHER = "3";

    /**
     * 判断请求平台
     * @param request req
     * @return 平台枚举
     */
    public static String getPlatform(HttpServletRequest request){
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if(userAgent.contains("micromessenger")){
            //微信
            return TYPE_OF_MICRO_MESSAGE;
        }else if(userAgent.contains("android") ){
            //安卓
            return TYPE_OF_ANDROID;
        }else if(userAgent.contains("iphone")  || userAgent.contains("ipad")  || userAgent.contains("ipod") ){
            //苹果
            return TYPE_OF_IOS;
        }else{
            //电脑
            return TYPE_OF_OTHER;
        }
    }
}
