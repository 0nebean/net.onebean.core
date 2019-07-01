package net.onebean.core.extend;

import com.eakay.util.PropUtil;

/**
 * 从配置中心初始化logback配置类
 * @author 0neBean
 */
public class ApolloConfInitializer {

    private final static String LOG_CONF_SYSTEM_PROPERTY_KEY = "logging.config";
    private final static String LOG_PATH_SYSTEM_PROPERTY_KEY = "logging.path";
    private final static String LOG_LEVEL_SYSTEM_PROPERTY_KEY = "logging.global.level";
    private final static String APOLLO_ENABLE_SYSTEM_PROPERTY_KEY = "apollo.bootstrap.enabled";
    private final static String APOLLO_NAMESPACES_SYSTEM_PROPERTY_KEY = "apollo.bootstrap.namespaces";


    public static void init(){
        initApolloConf();
        initLogBack();
    }

    /**
     * 初始化apollo配置读取
     */
    private static void initApolloConf(){
        String apolloEnable = PropUtil.getInstance().getConfig(APOLLO_ENABLE_SYSTEM_PROPERTY_KEY,PropUtil.PUBLIC_CONF_APOLLO_INITIALIZER);
        System.setProperty(APOLLO_ENABLE_SYSTEM_PROPERTY_KEY,apolloEnable);
        String apolloNameSpaces = PropUtil.getInstance().getConfig(APOLLO_NAMESPACES_SYSTEM_PROPERTY_KEY,PropUtil.PUBLIC_CONF_APOLLO_INITIALIZER);
        System.setProperty(APOLLO_NAMESPACES_SYSTEM_PROPERTY_KEY,apolloNameSpaces);
    }

    /**
     * 从配置中心初始化 logback
     */
    private static void initLogBack(){
        String loggingConf = PropUtil.getInstance().getConfig(LOG_CONF_SYSTEM_PROPERTY_KEY,PropUtil.PUBLIC_CONF_LOG);
        System.setProperty(LOG_CONF_SYSTEM_PROPERTY_KEY,loggingConf);
        String loggingPath = PropUtil.getInstance().getConfig(LOG_PATH_SYSTEM_PROPERTY_KEY,PropUtil.PUBLIC_CONF_LOG);
        System.setProperty(LOG_PATH_SYSTEM_PROPERTY_KEY,loggingPath);
        String loggingGlobalLevel = PropUtil.getInstance().getConfig(LOG_LEVEL_SYSTEM_PROPERTY_KEY,PropUtil.PUBLIC_CONF_LOG);
        System.setProperty(LOG_LEVEL_SYSTEM_PROPERTY_KEY,loggingGlobalLevel);

    }


}
