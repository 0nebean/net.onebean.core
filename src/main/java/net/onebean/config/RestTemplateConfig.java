package net.onebean.config;

import net.onebean.core.form.Parse;
import net.onebean.util.PropUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * rest 配置类
 * @author World
 */
@Configuration
@ConditionalOnClass(value = {RestTemplate.class})
public class RestTemplateConfig {

    private static final String SIMPLE_CLIENT_HTTP_REQUEST_FACTORY_READ_TIMEOUT = "simple.client.http.request.factory.read.timeout";
    private static final String SIMPLE_CLIENT_HTTP_REQUEST_FACTORY_CONNECT_TIMEOUT = "simple.client.http.request.factory.connect.timeout";

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Parse.toInt(PropUtil.getInstance().getConfig(SIMPLE_CLIENT_HTTP_REQUEST_FACTORY_READ_TIMEOUT, PropUtil.PUBLIC_CONF_SYSTEM)));
        factory.setReadTimeout(Parse.toInt(PropUtil.getInstance().getConfig(SIMPLE_CLIENT_HTTP_REQUEST_FACTORY_CONNECT_TIMEOUT, PropUtil.PUBLIC_CONF_SYSTEM)));
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory){
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

}
