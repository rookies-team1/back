package com.alreadyemployee.alreadyemployee.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class RestClientConfig {

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
                .baseUrl(llmBaseUrl)
                .build();
    }

    @PostConstruct
    public void debugLLM() {
        System.out.println("🔥🔥🔥 LLM base-url = " + llmBaseUrl);
    }
}
