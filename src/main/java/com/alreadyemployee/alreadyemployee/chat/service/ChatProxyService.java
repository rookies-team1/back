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

@Service
@RequiredArgsConstructor
public class ChatProxyService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final ChatSessionRepository chatSessionRepository;

    @Value("${llm.base-url}")
    private String llmBaseUrl;

    @Transactional
    public ChatResponseDTO proxyChatRequest(String jsonRequest, MultipartFile file, CustomUserDetails userDetails) {
        ChatRequestDTO chatRequestDTO = parseJsonRequest(jsonRequest);

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userDetails.getId().toString()));

        News news = newsRepository.findById(chatRequestDTO.getNewsId().longValue())
                .orElseThrow(() -> new BusinessException(ErrorCode.NEWS_NOT_FOUND, String.valueOf(chatRequestDTO.getNewsId())));

        chatSessionRepository.findByUserAndNews(user, news)
                .orElseGet(() -> chatSessionRepository.save(ChatSession.builder().user(user).news(news).build()));

        MultiValueMap<String, Object> body = createMultipartBody(jsonRequest, file);
        return sendRequestToFastApi(body);
    }

    private ChatRequestDTO parseJsonRequest(String jsonRequest) {
        try {
            return objectMapper.readValue(jsonRequest, ChatRequestDTO.class);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_JSON_FORMAT, e.getMessage());
        }
    }

    private MultiValueMap<String, Object> createMultipartBody(String jsonRequest, MultipartFile file) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("request", jsonRequest);
        if (file != null && !file.isEmpty()) {
            body.add("file", file.getResource());
        }
        return body;
    }

    private ChatResponseDTO sendRequestToFastApi(MultiValueMap<String, Object> body) {
        try {
            return restClient.post()
                    .uri("/chat")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(ChatResponseDTO.class);
        } catch (HttpStatusCodeException e) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR, String.format("FastAPI 오류: %s - %s", e.getStatusCode(), e.getResponseBodyAsString()));
        } catch (RestClientException e) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR, "FastAPI 통신 실패: " + e.getMessage());
        }
    }
}
