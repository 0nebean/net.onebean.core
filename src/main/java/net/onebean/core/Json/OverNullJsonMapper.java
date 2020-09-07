package net.onebean.core.Json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 理序列化过程中的Null值，把值替换成""空字符串
 * @author World
 */
public class OverNullJsonMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	public OverNullJsonMapper() {
		// this(Include.NON_EMPTY);
		// 空值处理为空串
		this.getSerializerProvider().setNullValueSerializer(
				new JsonSerializer<Object>() {
					@Override
					public void serialize(Object value, JsonGenerator jgen,
							SerializerProvider provider) throws IOException,
							JsonProcessingException {
						jgen.writeString("");
					}
				});
	}
}
