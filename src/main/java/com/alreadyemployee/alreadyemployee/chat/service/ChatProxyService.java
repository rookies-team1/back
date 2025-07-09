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

/**
 * FastAPI 서버와 통신하는 프록시 서비스
 * 채팅 요청을 FastAPI 서버로 전달하고 응답을 받아오는 역할을 담당
 */
@RequiredArgsConstructor
@Service
public class ChatProxyService {

    /**
     * application.properties에서 FastAPI 서버 URL을 가져옴
     */
    @Value("${fastapi.server.url}")
    private String fastApiBaseUrl;

    /**
     * HTTP 요청을 수행하기 위한 RestTemplate 인스턴스
     */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 채팅 요청과 파일을 FastAPI 서버로 전송하는 메서드
     *
     * @param jsonRequest JSON 형식의 채팅 요청 문자열
     * @param file 선택적으로 전송할 파일 (null 가능)
     * @return FastAPI 서버의 응답 문자열
     * @throws BusinessException FastAPI 서버 통신 중 오류 발생 시
     */
    public String sendMultipartChat(String jsonRequest, MultipartFile file) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("request", jsonRequest);

            // 파일이 존재하고 비어있지 않은 경우 파일도 요청에 추가
            if (file != null && !file.isEmpty()) {
                // MultipartFile을 Resource로 변환하여 요청에 포함
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

            // multipart/form-data 형식으로 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // HTTP 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // FastAPI 서버의 채팅 엔드포인트 URL 구성
            String targetUrl = fastApiBaseUrl + "/chat";

            // POST 요청 전송 및 응답 수신
            ResponseEntity<String> response = restTemplate.postForEntity(
                    targetUrl,
                    requestEntity,
                    String.class
            );

            // 성공적인 응답(2xx)인 경우 응답 본문 반환
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                // 서버에서 오류 응답을 받은 경우 예외 발생
                throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR, "요약 서버에서 실패 응답 수신");
            }

        } catch (IOException e) {
            // 파일 처리 중 발생한 입출력 예외
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR, "파일 처리 중 오류 발생: " + e.getMessage());
        } catch (Exception e) {
            // 다른 모든 예외 처리
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR, e.getMessage());
        }
    }
}