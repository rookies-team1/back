package com.alreadyemployee.alreadyemployee.chat.service;

import com.alreadyemployee.alreadyemployee.exception.BusinessException;
import com.alreadyemployee.alreadyemployee.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ChatProxyService {

    @Value("${fastapi.server.url}")
    private String fastApiBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendMultipartChat(String jsonRequest, MultipartFile file) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("request", jsonRequest);

            if (file != null && !file.isEmpty()) {
                Resource fileResource = new InputStreamResource(file.getInputStream()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }

                    @Override
                    public long contentLength() throws IOException {
                        return file.getSize();
                    }
                };
                body.add("file", fileResource);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // baseUrl을 사용해서 유연하게 요청
            String targetUrl = fastApiBaseUrl + "/chat";

            ResponseEntity<String> response = restTemplate.postForEntity(
                    targetUrl,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR, "요약 서버에서 실패 응답 수신");
            }

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR, "파일 변환 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR, e.getMessage());
        }
    }
}
