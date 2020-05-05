package net.onebean.util;

import net.onebean.core.extend.ApolloConfInitializer;
import net.onebean.core.metadata.PropertiesLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件读取类
 * @author 0neBean
 */
public class PropUtil {

    public final static String DEFLAULT_NAME_SPACE = "application";
    public final static String PUBLIC_CONF_SYSTEM = "system";
    public final static String PUBLIC_CONF_JDBC = "public-conf.jdbc";
    public final static String PUBLIC_CONF_REDIS = "public-conf.redis";
    public final static String PUBLIC_CONF_MONGODB = "public-conf.mongdb";
    public final static String PUBLIC_CONF_FREEMARKER = "public-conf.freemarker";
    public final static String PUBLIC_CONF_SPRING_CLOUD = "public-conf.spring-cloud";
    public final static String PUBLIC_CONF_RABBIT_MQ = "public-conf.rabbitmq";
    public final static String PUBLIC_CONF_ALIYUN = "public-conf.aliyun";
    public final static String PUBLIC_CONF_SSO = "public-conf.sso";

    public static String isActiveApolloConfig = null;


    /*
    * 获取apollo注册地址
    */
    private static Boolean isActiveRemoteConfig(){
        if (null == isActiveApolloConfig) {
            isActiveApolloConfig = System.getProperty(ApolloConfInitializer.SPRING_CONFIG_ACTIVE_APOLLO);
            if (null == isActiveApolloConfig) {
                isActiveApolloConfig = "";
            }
        }
        return StringUtils.isEmpty(isActiveApolloConfig) || isActiveApolloConfig.equals("true")  ;
    }

    private PropUtil() {
        initPropertiesLoader();
    }

    /**
     * 初始化配置文件
     */
    private void initPropertiesLoader(){
        loaderMap = new HashMap<>();
        loaderMap.put(DEFLAULT_NAME_SPACE,new PropertiesLoader(DEFLAULT_NAME_SPACE+suffix));
    }

    /**
     * 当前对象实例
     */
    private static PropUtil propUtil = new PropUtil();
    /**
     * 获取当前对象实例
     * @return PropUtil
     */
    public static PropUtil getInstance() {
        return propUtil;
    }
    /**
     *  文件名
     */
    private final static String suffix = ".properties";
    /**
     * 属性文件加载对象map
     */
    private Map<String,PropertiesLoader> loaderMap = null;

    /**
     * 性配置文件获取配置
     * @param key 键
     * @param nameSpace 配置文件名
     * @return 配置
     */
    private String getConfigInLoader(String key,String nameSpace) {
        PropertiesLoader propertiesLoader;
        String value;
        if (null == loaderMap.get(nameSpace)){
            propertiesLoader = new PropertiesLoader(nameSpace+suffix);
            loaderMap.put(nameSpace,propertiesLoader);
        }else{
            propertiesLoader = loaderMap.get(nameSpace);
        }
        value = propertiesLoader.getProperty(key);
        return StringUtils.isEmpty(value)?StringUtils.EMPTY:value;
    }


    /**
     * 获取配置
     * @param key key
     * @param nameSpace ns
     * @return value
     */
    public String getConfig(String key,String nameSpace) {
        String value = getConfigInLoader(key,nameSpace);
        if (StringUtils.isEmpty(value) && isActiveRemoteConfig()){
            try {
                value = ApolloPropUtils.getString(key,nameSpace);
            } catch (Exception ignored) {
            }
        }
        return value;
    }



}
