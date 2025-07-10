package com.alreadyemployee.alreadyemployee.chat.controller;

import com.alreadyemployee.alreadyemployee.chat.controller.dto.ChatRequestDTO;
import com.alreadyemployee.alreadyemployee.chat.controller.dto.NewsByIdDTO;
import com.alreadyemployee.alreadyemployee.chat.service.ChatService;
import com.alreadyemployee.alreadyemployee.chat.service.ChatProxyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final ChatProxyService chatProxyService;

    /**
     * 파일을 포함한 채팅 요청을 처리하는 API 엔드포인트
     * @param jsonRequest JSON 형식의 요청 문자열
     * @param file 선택적으로 업로드할 파일
     * @return 채팅 응답
     * @throws Exception 처리 중 오류 발생 시
     */
    @PostMapping(value = "/chat-with-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> chatWithFile(
            @RequestPart("request") String jsonRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {
        // 받은 jsonRequest 문자열을 그대로 서비스로 전달
        String response = chatProxyService.sendMultipartChat(jsonRequest, file);
        return ResponseEntity.ok(response);
    }
}
