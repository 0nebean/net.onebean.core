package net.onebean.core.Json;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * jockson反序列化时间转换插件
 * @author 0neBean
 */
public class TimeStamp_Deserializer extends JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = jp.getText();
        try {
            return new Timestamp(format.parse(date).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}