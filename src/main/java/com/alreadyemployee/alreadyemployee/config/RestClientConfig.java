package com.alreadyemployee.alreadyemployee.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import org.springframework.beans.factory.annotation.Value;

@Profile("prod")
@Configuration
@Slf4j
public class RestClientConfig {


//    @Bean
//    public RestClient restClient(RestClient.Builder builder) {
//        // Java(camelCase) <-> Python(snake_case) 자동 변환을 위한 ObjectMapper 설정
//        ObjectMapper objectMapper = new ObjectMapper()
//                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//
//        // 설정된 ObjectMapper를 사용하는 MessageConverter 생성
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
//
//        return builder
//                .baseUrl("http://localhost:8000")
//                .messageConverters(converters -> converters.add(0, converter)) // 기본 컨버터보다 우선 적용
//                .build();
//    }

    @Value("${llm.base-url}")
    private String llmBaseUrl;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        log.info("🔥🔥🔥 LLM base-url = {}" , llmBaseUrl);

        return builder
                .messageConverters(converters -> {
                    converters.add(new MappingJackson2HttpMessageConverter()); // 🔥 JSON 변환기 등록
                })
                //llm-svc 가 로컬에서 8000 포트로 띄위져 있을 때의 URL 설정
                .baseUrl("http://llm-svc:8000")
                .build();
    }

    @PostConstruct
    public void debugLLM() {
        System.out.println("🔥🔥🔥 LLM base-url = " + llmBaseUrl);
    }

}