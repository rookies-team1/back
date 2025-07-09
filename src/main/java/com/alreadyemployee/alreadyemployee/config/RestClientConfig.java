package com.alreadyemployee.alreadyemployee.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        // Java(camelCase) <-> Python(snake_case) 자동 변환을 위한 ObjectMapper 설정
        ObjectMapper objectMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        // 설정된 ObjectMapper를 사용하는 MessageConverter 생성
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);

        return builder
                .baseUrl("http://localhost:8000")
                .messageConverters(converters -> converters.add(0, converter)) // 기본 컨버터보다 우선 적용
                .build();
    }
}