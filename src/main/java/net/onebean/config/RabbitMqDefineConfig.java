package net.onebean.config;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


/**
 * @Auther 0neBean
 * spring 初始化mq配置
 */
@Component
public class RabbitMqDefineConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    private final static Logger logger = (Logger) LoggerFactory.getLogger(RabbitMqDefineConfig.class);

    //0neBean:当一个ApplicationContext被初始化或刷新触发 加载字典到内存中
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("start init rabbit mq define config");
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if(!ack) {
                logger.info("rabbitTemplate send error" + cause + correlationData.toString());
            } else {
                logger.info("rabbitTemplate send success");
            }
        });
        logger.info("init rabbit mq define config done");
    }



}