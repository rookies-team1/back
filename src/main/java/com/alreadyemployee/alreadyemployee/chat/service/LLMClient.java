package com.alreadyemployee.alreadyemployee.chat.service;


import com.alreadyemployee.alreadyemployee.chat.dto.ChatToLLMRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;

//RestClientConfig 설정파일에서 baseUrl을 "http://localhost:8000"으로 정해뒀음
//해당 클래스는 llm-svc라는 api 서버에 HTTP 요청을 보내는 역할
@Component
@RequiredArgsConstructor
public class LLMClient {

    private final RestClient restClient;

    public String sendToLLM(String question, List<String> history) {
        ChatToLLMRequest req = ChatToLLMRequest.builder()
                .question(question)
                .chatHistory(history)
                .build();

        String jsonRequest;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonRequest = objectMapper.writeValueAsString(req);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert ChatToLLMRequest to JSON", e);
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpHeaders stringHeaders = new HttpHeaders();
        stringHeaders.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> requestPart = new HttpEntity<>(jsonRequest, stringHeaders);
        body.add("request", requestPart);

        return restClient.post()
                .uri("/chat")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(String.class);

    }

}
