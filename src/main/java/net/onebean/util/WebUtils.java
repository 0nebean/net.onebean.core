package net.onebean.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

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


    /**
     * 从HttpServletRequest中获取参数对象
     * @param request req
     * @param clazz 类型
     * @return obj
     */
    public static  <T> T getParamVoFromHttpServletRequest(HttpServletRequest request,Class<T> clazz) {
        JSONObject jsonObject = new JSONObject();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length >0) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    jsonObject.put(paramName, paramValue);
                }
            }
        }
        return JSONObject.parseObject(jsonObject.toJSONString(),clazz);
    }
}
