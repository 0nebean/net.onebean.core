package net.onebean.util;

import com.ctrip.framework.apollo.ConfigService;

/**
 * @author 0neBean
 * Apollo配置中心工具类
 */

public class ApolloPropUtils {

    public static String getString(String key, String namespace){
        if (StringUtils.isEmpty(key)){
            key = "";
        }
        if (StringUtils.isEmpty(namespace)){
            namespace = "";
        }
        return ConfigService.getConfig(namespace).getProperty(key, null);
    }

    public static String getString(String key){
        if (StringUtils.isEmpty(key)){
            key = "";
        }
        return ConfigService.getAppConfig().getProperty(key,null);
    }


}
