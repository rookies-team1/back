package com.alreadyemployee.alreadyemployee.chat.service;

import com.alreadyemployee.alreadyemployee.chat.dto.ChatToLLMRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

//RestClientConfig 설정파일에서 baseUrl을 "http://localhost:8000"으로 정해뒀음
//해당 클래스는 llm-svc라는 api 서버에 HTTP 요청을 보내는 역할
@Component
@RequiredArgsConstructor
public class LLMClient {

    private final RestClient restClient;

    // question(사용자 질문), chatHistory(이전 대화 내역)을
    // ChatToLLMRequest DTO로 묶어 API 서버에 POST 요청을 보냄
    // 그리고 서버로부터 받은 응답 문자열을 그대로 반환
    public String sendToLLM(String question, List<String> history) {
        ChatToLLMRequest req = ChatToLLMRequest.builder()
                .question(question)
                .chatHistory(history)
                .build();

        return restClient.post()
                .uri("/chat")
                .body(req)
                .retrieve()
                .body(String.class);
    }
}
