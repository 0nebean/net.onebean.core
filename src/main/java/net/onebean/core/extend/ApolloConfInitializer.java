package net.onebean.core.extend;

import ch.qos.logback.classic.Logger;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import net.onebean.util.ClassUtils;
import net.onebean.util.PropUtil;
import net.onebean.util.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 从配置中心初始化logback配置类
 * @author 0neBean
 */
public class ApolloConfInitializer {

    private static Logger logger = (Logger) LoggerFactory.getLogger(ApolloConfInitializer.class);

    private final static String SEPARATOR = System.getProperty("file.separator");
    private final static String APOLLO_BOOTSTRAP_ENABLED = "apollo.bootstrap.enabled";
    private final static String APOLLO_BOOTSTRAP_NAMESPACES = "apollo.bootstrap.namespaces";
    private final static String SPRING_CONFIG_NAME = "spring.config.name";
    private final static String LOGGING_CONFIG = "logging.config";
    private final static String LOGGING_LEVEL = "logging.level.root";
    private final static String APP_ID = "spring.application.name";
    private final static String APOLLO_META_URL_SUFFIX = ".meta";
    private final static String APP_ID_KEY = "app.id";
    private final static String LOGGING_PATH = "logging.path";
    private final static String EVN_KEY = "env";
    private final static String CONFIG_SERVICE = "apollo.configService";
    public final static String SPRING_CONFIG_ACTIVE_APOLLO = "spring.config.active.apollo";

    public static void init(){
        Boolean springConfigActiveLocal = ClassUtils.hasAnnotation(ClassUtils.getBasicCallerClassName(), EnableApolloConfig.class.getSimpleName());
        String apolloBootstrapNamespaces = PropUtil.getInstance().getConfig(APOLLO_BOOTSTRAP_NAMESPACES,PropUtil.PUBLIC_CONF_SYSTEM);
        System.getProperties().setProperty(SPRING_CONFIG_ACTIVE_APOLLO,String.valueOf(springConfigActiveLocal));

        if (springConfigActiveLocal){
            String env = System.getProperty(EVN_KEY);
            if (StringUtils.isEmpty(env)){
                env = "dev";
                System.getProperties().setProperty(EVN_KEY,env);
            }
            String apolloBootstrapEnabled = PropUtil.getInstance().getConfig(APOLLO_BOOTSTRAP_ENABLED,PropUtil.PUBLIC_CONF_SYSTEM);

            String loggingConfig = PropUtil.getInstance().getConfig(LOGGING_CONFIG,PropUtil.PUBLIC_CONF_SYSTEM);
            String loggingPath = PropUtil.getInstance().getConfig(LOGGING_PATH,PropUtil.PUBLIC_CONF_SYSTEM);
            String apolloMeta = PropUtil.getInstance().getConfig(env.toLowerCase()+ APOLLO_META_URL_SUFFIX,PropUtil.PUBLIC_CONF_SYSTEM);
            String appId = PropUtil.getInstance().getConfig(APP_ID,PropUtil.DEFLAULT_NAME_SPACE);
            String classPathAbsolutePath = null;
            try {
                classPathAbsolutePath = new File("").getCanonicalPath();
            } catch (IOException e) {
                logger.error("can not get file path from system,e = ",e);
            }

            logger.info("ApolloConfInitializer init classPathAbsolutePath = "+classPathAbsolutePath);
            /*windows bat startup*/
            if (classPathAbsolutePath.endsWith(SEPARATOR + "bin")) {
                loggingPath = "."+loggingPath;
            }

            System.getProperties().setProperty(LOGGING_PATH,loggingPath);
            System.getProperties().setProperty(CONFIG_SERVICE,apolloMeta);
            logger.info(CONFIG_SERVICE + " = "+System.getProperty(CONFIG_SERVICE));
            System.getProperties().setProperty(APOLLO_BOOTSTRAP_ENABLED,apolloBootstrapEnabled);
            System.getProperties().setProperty(APOLLO_BOOTSTRAP_NAMESPACES,apolloBootstrapNamespaces);
            System.getProperties().setProperty(LOGGING_CONFIG,loggingConfig);
            System.getProperties().setProperty(APP_ID_KEY,appId);
            String loggingLevel = PropUtil.getInstance().getConfig(LOGGING_LEVEL,PropUtil.DEFLAULT_NAME_SPACE);
            if (StringUtils.isNotEmpty(loggingLevel)){
                System.getProperties().setProperty(LOGGING_LEVEL,loggingLevel);
            }
        }else{
            System.getProperties().setProperty(SPRING_CONFIG_NAME,apolloBootstrapNamespaces);
        }
    }

}
