package net.onebean.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 
 * <h6>处理序列化过程中的Null值，把值替换成""空字符串</h6>
 *
 * User: Qiang <br/>
 * Describe： <br/>
 *
 * @date 2015年10月21日 下午4:07:22
 */
public class OverrNullJsonMapper extends ObjectMapper {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OverrNullJsonMapper() {
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
