package com.alreadyemployee.alreadyemployee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                //llm-svc 가 로컬에서 8000 포트로 띄위져 있을 때의 URL 설정
                .baseUrl("http://llm-svc:8000")
                .build();
    }
}
