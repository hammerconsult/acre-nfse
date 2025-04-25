package br.com.webpublico.config;

import br.com.webpublico.service.AbstractWPService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class JacksonConfiguration {

    private final Logger log = LoggerFactory.getLogger(AbstractWPService.class);
    private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
    private static final SimpleDateFormat FORMAT_DATE_ALTERNATIVE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Bean
    @Primary
    public ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Date.class, new DateSerializer());
        javaTimeModule.addDeserializer(Date.class, new DateDeserializer());


        mapper.registerModule(javaTimeModule);

        return mapper;
    }

    public class DateSerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                gen.writeString(FORMAT_DATE.format(value));
            } else {
                gen.writeNull();
            }
        }
    }

    public class DateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            try {
                if (Strings.isNullOrEmpty(p.getValueAsString())) {
                    return null;
                }
                return FORMAT_DATE.parse(p.getValueAsString());
            } catch (ParseException e) {
                try {
                    return FORMAT_DATE_ALTERNATIVE.parse(p.getValueAsString());
                } catch (ParseException ex) {
                    log.error("Erro ao deserializar data {}", p.getValueAsString());
                }
            }
            return null;
        }
    }
}