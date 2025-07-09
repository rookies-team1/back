package com.alreadyemployee.alreadyemployee.chat.service;

import com.alreadyemployee.alreadyemployee.chat.dto.ChatToLLMRequest;
import com.alreadyemployee.alreadyemployee.chat.util.MultipartInputStreamFileResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
//                .uri("/chat")
                .body(req)
                .retrieve()
                .body(String.class);
    }


    public String sendMultipartToLLM(
                //Long sessionId,
                //Long userId,
                String userInputText,
                List<MultipartFile> userInputFiles
                //Long chatMessageId,
                //Long newsId,
                //String companyName,
                //String chatHistoriesJson
        ) throws IOException {

            // 1. 멀티파트 요청 구성
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            //body.add("sessionId", sessionId);
            //body.add("userId", userId);
            if (userInputText != null) body.add("userInputText", userInputText);
            if (userInputFiles != null) {
                for(MultipartFile file : userInputFiles) {
                    body.add("userInputFile", new MultipartInputStreamFileResource(
                                    file.getInputStream(),
                                    file.getOriginalFilename()
                    ));
                }
            }
            //body.add("chatMessageId", chatMessageId);
            //body.add("newsId", newsId);
            //body.add("companyName", companyName);
            //if (chatHistoriesJson != null) body.add("chatHistories", chatHistoriesJson);

            // 2. 전송
            return
                    restClient.post() //Post 요청으로 반환
                    //.uri("/api/llm") //uri: baseUrl 뒤에 붙는 url
                    //요청 헤더 설정: "Content-Type":"multipart/form-data"
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    //요청 body 설정: 위에서 구성한 멀티파트 요청 구성요소를 body에 넣어줌
                    .body(body)
                    //요청(request)을 보내고 응답(response)을 받을 준비를 함
                    .retrieve()
                    //응답 결과를 String으로 파싱해서 반환
                    .body(String.class);
        }
}
