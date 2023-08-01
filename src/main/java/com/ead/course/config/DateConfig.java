package com.ead.course.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.format.DateTimeFormatter;

/**
 * Classe responsavel por realizar toda a configuração a nivel global do padrão da ISO-8601 UTC
 * Verificar conflitos quando se utiliza specializaçoes na classe, ou um ou outro.
 */
//@Configuration
public class DateConfig {

    private final static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private LocalDateTimeSerializer dateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN));

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(dateTimeSerializer);

        return new ObjectMapper().registerModule(javaTimeModule);
    }
}
