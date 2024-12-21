package com.douyu.live.framework.redis.starter.config;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.cache.support.NullValue;
import org.springframework.util.StringUtils;
import java.io.IOException;
/**
 * @author luiguanyi
 * * @date 2024/12/21
 */
public class MapperFactory {
    public static ObjectMapper newInstance() {
        return initMapper(new ObjectMapper(), (String) null);
    }
    private static ObjectMapper initMapper(ObjectMapper mapper,
                                           String classPropertyTypeName) {
        mapper.registerModule(new SimpleModule().addSerializer(new
                MapperNullValueSerializer(classPropertyTypeName)));
        if (StringUtils.hasText(classPropertyTypeName)) {
            mapper.enableDefaultTypingAsProperty(DefaultTyping.NON_FINAL,
                    classPropertyTypeName);
        } else {
            mapper.enableDefaultTyping(DefaultTyping.NON_FINAL,
                    As.PROPERTY);
        }
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }
    /**
     * {@link StdSerializer} adding class information required by
     default typing. This allows de-/serialization of
     * {@link NullValue}.
     *
     * @author Christoph Strobl
     * @since 1.8
     */
    private static class MapperNullValueSerializer extends
            StdSerializer<NullValue> {
        private static final long serialVersionUID =
                1999052150548658808L;
        private final String classIdentifier;
        /**
         * @param classIdentifier can be {@literal null} and will
        be defaulted to {@code @class}.
         */
        MapperNullValueSerializer(String classIdentifier) {
            super(NullValue.class);
            this.classIdentifier =
                    StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
        }
        /*
        * (non-Javadoc)
        * @see
        com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(jav
        a.lang.Object, com.fasterxml.jackson.core.JsonGenerator,
        com.fasterxml.jackson.databind.SerializerProvider)
        */
        @Override
        public void serialize(NullValue value, JsonGenerator jgen,
                              SerializerProvider provider)
                throws IOException {
            jgen.writeStartObject();
            jgen.writeStringField(classIdentifier,
                    NullValue.class.getName());
            jgen.writeEndObject();
        }
    }
}
