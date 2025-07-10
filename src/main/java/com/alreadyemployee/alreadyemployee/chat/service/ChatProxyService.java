package com.alreadyemployee.alreadyemployee.chat.service;

import com.alreadyemployee.alreadyemployee.chat.controller.dto.ChatRequestDTO;
import com.alreadyemployee.alreadyemployee.chat.controller.dto.ChatResponseDTO;
import com.alreadyemployee.alreadyemployee.chat.entity.ChatSession;
import com.alreadyemployee.alreadyemployee.chat.repository.ChatSessionRepository;
import com.alreadyemployee.alreadyemployee.exception.BusinessException;
import com.alreadyemployee.alreadyemployee.exception.ErrorCode;
import com.alreadyemployee.alreadyemployee.news.entity.News;
import com.alreadyemployee.alreadyemployee.news.repository.NewsRepository;
import com.alreadyemployee.alreadyemployee.user.entity.CustomUserDetails;
import com.alreadyemployee.alreadyemployee.user.entity.User;
import com.alreadyemployee.alreadyemployee.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ChatProxyService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final ChatSessionRepository chatSessionRepository;

    @Value("${fastapi.server.url}")
    private String fastapiServerUrl;

    @Transactional
    public ChatResponseDTO proxyChatRequest(
            String jsonRequest, MultipartFile file, CustomUserDetails userDetails
    ) {
        // 1. JSON 요청 파싱
        ChatRequestDTO chatRequestDTO = parseJsonRequest(jsonRequest);

        // 2. DB에서 User와 News 엔티티 조회
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND, userDetails.getId().toString()
                ));
        News news = newsRepository.findById(chatRequestDTO.getNewsId().longValue()) // Integer -> Long 변환
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.NEWS_NOT_FOUND, String.valueOf(chatRequestDTO.getNewsId())
                ));

        // 3. ChatSession 조회 또는 생성
        ChatSession session = chatSessionRepository.findByUserAndNews(user, news)
                .orElseGet(() -> createNewChatSession(user, news));

        // 4. Python 서버로 보낼 최종 요청 데이터 구성
        //    - 인증된 사용자 ID와 조회된 세션 ID로 요청 데이터를 보강
        // 기존 jsonRequest를 그대로 사용하므로 주석 처리

        // 5. 멀티파트 본문 생성
        MultiValueMap<String, Object> body = createMultipartBody(jsonRequest, file);

        // 6. Python FastAPI 서버로 요청 전송 및 응답 반환
        return sendRequestToFastApi(body);
    }

    private ChatRequestDTO parseJsonRequest(String jsonRequest) {
        try {
            return objectMapper.readValue(jsonRequest, ChatRequestDTO.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INVALID_JSON_FORMAT, e.getMessage());
        }
    }

    private ChatSession createNewChatSession(User user, News news) {
        ChatSession newSession = ChatSession.builder()
                .user(user)
                .news(news)
                .build();
        return chatSessionRepository.save(newSession);
    }

    private MultiValueMap<String, Object> createMultipartBody(String jsonRequest, MultipartFile file) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("request", jsonRequest);

        if (file != null && !file.isEmpty()) {
            try {
                body.add("file", file.getResource());
            } catch (Exception e) {
                // 파일 관련 오류 처리
                throw new BusinessException(ErrorCode.FILE_PROCESSING_ERROR, "파일 리소스 접근 중 오류: " + e.getMessage());
            }
        }
        return body;
    }

    private ChatResponseDTO sendRequestToFastApi(MultiValueMap<String, Object> body) {
        String fastapiEndpoint = fastapiServerUrl + "/chat";
        try {
            ChatResponseDTO response = restClient.post()
                    .uri(fastapiEndpoint)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(ChatResponseDTO.class);
            return response;
        } catch (HttpStatusCodeException e) {
            // FastAPI 서버가 4xx, 5xx 응답을 반환한 경우
            String errorMessage = String.format("FastAPI 오류: %s, 응답: %s", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR, errorMessage);
        } catch (RestClientException e) {
            // 네트워크 오류 등 통신 자체에 문제가 발생한 경우
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR, "FastAPI 서버 통신 실패: " + e.getMessage());
        }
    }
}