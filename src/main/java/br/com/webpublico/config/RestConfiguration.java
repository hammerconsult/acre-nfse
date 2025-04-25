package br.com.webpublico.config;

import br.com.webpublico.security.ApiKeyRequestInterceptor;
import br.com.webpublico.service.AbstractWPService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class RestConfiguration {

    private final Logger log = LoggerFactory.getLogger(RestConfiguration.class);
    private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${webpublico.api-key}")
    private String apiKey;

    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(Date.class, new DateDeserializer());
        objectMapper.registerModule(javaTimeModule);
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(Lists.newArrayList(new ByteArrayHttpMessageConverter(),
                new StringHttpMessageConverter(),
                mappingJackson2HttpMessageConverter()));
        restTemplate.getInterceptors().add(new ApiKeyRequestInterceptor(apiKey));
        return restTemplate;
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
                log.error("Erro ao deserializar data {}", p.getValueAsString());
            }
            return null;
        }
    }

    @Bean
    public RestTemplate restTemplateWebReport() {
        return new RestTemplate();
    }

}
