package org.lhh.spongehome.config;


import com.fasterxml.jackson.databind.ObjectMapper;


import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * SpringMVC JSON配置类
 * 把长整型数字转换成字符串进行返回
 */
@JsonComponent
public class JsonConfig {
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
       ObjectMapper objectMapper = builder.createXmlMapper(false).build();
       SimpleModule module = new SimpleModule();
       module.addSerializer(Long.class, ToStringSerializer.instance);
       module.addSerializer(Long.TYPE, ToStringSerializer.instance);
       objectMapper.registerModule(module);
        return new ObjectMapper();
    }
}
