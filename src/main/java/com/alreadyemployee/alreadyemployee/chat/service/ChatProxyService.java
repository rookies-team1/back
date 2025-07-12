package com.alreadyemployee.alreadyemployee.chat.service;

import com.alreadyemployee.alreadyemployee.chat.controller.dto.ChatProxyRequestDTO;
import com.alreadyemployee.alreadyemployee.chat.controller.dto.ChatRequestDTO;
import com.alreadyemployee.alreadyemployee.chat.controller.dto.ChatResponseDTO;
import com.alreadyemployee.alreadyemployee.chat.entity.ChatSession;
import com.alreadyemployee.alreadyemployee.chat.repository.ChatSessionRepository;
import com.alreadyemployee.alreadyemployee.chat.util.MultipartInputStreamFileResource;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

    public ChatResponseDTO sendToPython(ChatProxyRequestDTO dto, MultipartFile file) {
        RestClient.RequestBodySpec req = restClient.post().uri("/chat");

        try {
            // ✅ JSON 변환은 공통
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(dto);

            // ✅ 항상 multipart/form-data 구성
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            form.add("request", json); // ✅ request는 무조건 넣음

            if (file != null && !file.isEmpty()) {
                form.add("file", new MultipartInputStreamFileResource(
                        file.getInputStream(), file.getOriginalFilename()));
            }
            // ✅ multipart/form-data 전송
            return req.body(form).retrieve().body(ChatResponseDTO.class);

        } catch (IOException e) {
            throw new RuntimeException("파일 전송 실패", e);
        }
    }

    private HttpHeaders jsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}