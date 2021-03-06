package net.onebean.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 结合apollo动态刷新日志级别
 * @author 0neBean
 */
@Configuration
@ConditionalOnProperty(name = "spring.config.active.apollo",havingValue = "true")
public class LogListenerConfig {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LoggerConfiguration.class);
    /**
     * 监听关键字，当配置中心的依次开头的配置发生变化时，日志级别刷新
     */
    private static final String LOGGER_TAG = "loggers.root.";

    @Autowired
    private LoggingSystem loggingSystem;

    /**
     * 可以指定具体的namespace，未指定时使用的是 application这个namespace
     */
    @ApolloConfig
    private Config config;

    @ApolloConfigChangeListener
    private void onChange(ConfigChangeEvent changeEvent) {
        refreshLoggingLevels();
    }

    @PostConstruct
    private void refreshLoggingLevels() {
        Set<String> keyNames = config.getPropertyNames();
        for (String key : keyNames) {
            if (containsIgnoreCase(key, LOGGER_TAG)) {
                String strLevel = config.getProperty(key, "info");
                LogLevel level = LogLevel.valueOf(strLevel.toUpperCase());
                //重置日志级别，马上生效
                //loggingSystem.setLogLevel(key.replace(LOGGER_TAG, ""), level);
                loggingSystem.setLogLevel("", level);
                LOGGER.info("{}:{}", key, strLevel);
            }
        }
    }

    private static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }
}