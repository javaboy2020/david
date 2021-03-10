package cn.smbms.tools;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName CustomObjectMapper
 * @Description TODO
 * @Author javaboy
 * @Date 2021/1/19 16:58
 * @Version 1.0
 **/
public class CustomObjectMapper extends ObjectMapper {

/*

    public CustomObjectMapper() {
        CustomSerializerFactory factory = new CustomSerializerFactory();
        factory.addGenericMapping(Date.class, new JsonSerializer<Date>() {
            public void serialize(Date value, JsonGenerator jsonGenerator,
                                  SerializerProvider provider) throws IOException, JsonProcessingException {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                jsonGenerator.writeString(sdf.format(value));
            }
        });
        this.setSerializerFactory(factory);
    }
*/

}
