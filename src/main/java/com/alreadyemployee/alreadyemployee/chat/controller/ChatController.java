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

    /**
     * 특정 뉴스를 요약하는 API 엔드포인트
     * @param newsId 요약할 뉴스의 ID
     * @return 뉴스 요약 결과 문자열
     */
//    @PostMapping("/summarize/{newsId}")
//    public String summarize(@PathVariable Long newsId) {
//        //DB에서 뉴스 데이터 가져오기
//        NewsByIdDTO newsById = chatService.getNewsById(newsId);
//
//        //파이썬 서버로 요청 보내기
//        String summary = chatService.summarizeNews(newsById);
//
//        //파이썬 서버에서 요약 결과를 받아 클라이언트로 응답
//        return summary;
//    }

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
