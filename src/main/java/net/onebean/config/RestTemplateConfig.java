package net.onebean.config;

import com.eakay.core.form.Parse;
import com.eakay.util.PropUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * rest 配置类
 * @author 0neBean
 */
@Configuration
@ConditionalOnClass(value = {RestTemplate.class})
public class RestTemplateConfig {

    private static final String SIMPLE_CLIENT_HTTP_REQUEST_FACTORY_READ_TIMEOUT = "simple.client.http.request.factory.read.timeout";
    private static final String SIMPLE_CLIENT_HTTP_REQUEST_FACTORY_CONNECT_TIMEOUT = "simple.client.http.request.factory.connect.timeout";

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Parse.toInt(PropUtil.getInstance().getConfig(SIMPLE_CLIENT_HTTP_REQUEST_FACTORY_READ_TIMEOUT, PropUtil.DEFLAULT_NAME_SPACE)));
        factory.setReadTimeout(Parse.toInt(PropUtil.getInstance().getConfig(SIMPLE_CLIENT_HTTP_REQUEST_FACTORY_CONNECT_TIMEOUT, PropUtil.DEFLAULT_NAME_SPACE)));
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory){
        return new RestTemplate(clientHttpRequestFactory);
    }

}
