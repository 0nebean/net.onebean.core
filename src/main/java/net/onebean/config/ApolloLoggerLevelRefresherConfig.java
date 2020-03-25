package net.onebean.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * apollo刷新日志级别 配置类
 * @author 0neBean
 */
@Configuration
@ConditionalOnProperty(name = "spring.config.active.apollo",havingValue = "true")
public class ApolloLoggerLevelRefresherConfig implements ApplicationContextAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApolloLoggerLevelRefresherConfig.class);

    private ApplicationContext applicationContext;

    @ApolloConfig
    private Config config;

    @PostConstruct
    private void initialize() {
        refreshLoggingLevels(config.getPropertyNames());
    }

    @ApolloConfigChangeListener(interestedKeys = {"logging.level.root"})
    private void onChange(ConfigChangeEvent changeEvent) {
        refreshLoggingLevels(changeEvent.changedKeys());
    }

    private void refreshLoggingLevels(Set<String> changedKeys) {
        LOGGER.info(" o0o0o0o0o0o0o0o0o0o0o0o0o0o0o0o0o0o0  Refreshing logging levels o0o0o0o0o0o0o0o0o0o0o0o0o0o0o0o0o0o0 ");
        /**
         * refresh logging levels
         * @see org.springframework.cloud.logging.LoggingRebinder#onApplicationEvent
         */
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changedKeys));

        LOGGER.info("Logging levels refreshed");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}