package net.onebean.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.eakay.util.ReflectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@Configuration
@ConditionalOnClass({ JSON.class })
public class FastJsonHttpMessageConvertersConfiguration {

	@Configuration
	@ConditionalOnClass({ FastJsonHttpMessageConverter.class })
	@ConditionalOnProperty(name = { "spring.http.converters.preferred-json-mapper" }, havingValue = "fastjson", matchIfMissing = true)
	protected static class FastJson2HttpMessageConverterConfiguration {

		protected FastJson2HttpMessageConverterConfiguration() {
		}

		@Bean
		@ConditionalOnMissingBean({ FastJsonHttpMessageConverter.class })
		public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
			FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

			FastJsonConfig fastJsonConfig = new FastJsonConfig();
			fastJsonConfig.setSerializerFeatures(
					SerializerFeature.PrettyFormat,
					//    输出key是包含双引号
					SerializerFeature.QuoteFieldNames,
					//    是否输出为null的字段,若为null 则显示该字段
					SerializerFeature.WriteMapNullValue,
					//    数值字段如果为null，则输出为0
					SerializerFeature.WriteNullNumberAsZero,
					//     List字段如果为null,输出为[],而非null
					SerializerFeature.WriteNullListAsEmpty,
					//    字符类型字段如果为null,输出为"",而非null
					SerializerFeature.WriteNullStringAsEmpty,
					//    Boolean字段如果为null,输出为false,而非null
					SerializerFeature.WriteNullBooleanAsFalse,
					//    Date的日期转换器
					SerializerFeature.WriteDateUseDateFormat,
					//    循环引用
					SerializerFeature.DisableCircularReferenceDetect
					);
			ValueFilter valueFilter = new ValueFilter() {
				public Object process(Object o, String s, Object o1) {
					Field field = ReflectionUtils.getField(o,s);
					if (null == o1 && field != null && field.getType().isAssignableFrom(Object.class)){
						o1 = new Object();
					}
					return o1;
				}
			};
			fastJsonConfig.setSerializeFilters(valueFilter);
			converter.setFastJsonConfig(fastJsonConfig);
			List<MediaType> supportedMediaTypes = new ArrayList<>();
			supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
			converter.setSupportedMediaTypes(supportedMediaTypes);
			return converter;
		}
	}


}
