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
//    private final ObjectMapper objectMapper; // ObjectMapper 주입
    //지금 이건 안돼요
    @PostMapping("/summarize/{newsId}")
    public String summarize(@PathVariable Long newsId) {
        //DB에서 뉴스 데이터 가져오기
        NewsByIdDTO newsById = chatService.getNewsById(newsId);

        //파이썬 서버로 요청 보내기
        String summary = chatService.summarizeNews(newsById);

        //파이썬 서버에서 요약 결과를 받아 클라이언트로 응답
        return summary;
    }

    @PostMapping(value = "/chat-with-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> chatWithFile(
            @RequestPart("request") String jsonRequest, // 다시 String으로 받습니다.
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {
        // 받은 jsonRequest 문자열을 그대로 서비스로 전달합니다.
        String response = chatProxyService.sendMultipartChat(jsonRequest, file);
        return ResponseEntity.ok(response);
    }
}
