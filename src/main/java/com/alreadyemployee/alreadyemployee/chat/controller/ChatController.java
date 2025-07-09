package com.alreadyemployee.alreadyemployee.chat.controller;


import com.alreadyemployee.alreadyemployee.chat.controller.dto.NewsByIdDTO;
import com.alreadyemployee.alreadyemployee.chat.controller.dto.PingResponseDTO;
import com.alreadyemployee.alreadyemployee.chat.controller.dto.SummarizeResponseDTO;
import com.alreadyemployee.alreadyemployee.chat.service.ChatService;
import com.alreadyemployee.alreadyemployee.chat.service.LLMClient;
import com.alreadyemployee.alreadyemployee.exception.global.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.List;

/**
 * 채팅 기능 테스트용 컨트롤러
 * - 클라이언트로부터 채팅 관련 데이터를 multipart/form-data로 받아 처리
 * - 실제 서비스 연동 전 DTO 처리 테스트 목적의 컨트롤러
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final LLMClient llmClient;
    private final RestClient restClient;
    private final ChatService chatService;

    @GetMapping("/ping")
    public PingResponseDTO ping(){
        return restClient.get().uri("/").retrieve().body(PingResponseDTO.class);
    }

    @PostMapping("/summarize/{newsId}")
    public SummarizeResponseDTO summarize(@PathVariable Long newsId){
        NewsByIdDTO newsById=chatService.getNewsById(newsId);
//        파이썬으로 요청 넘겨주기
        return restClient.post().uri("/summarize").body(newsById).retrieve().body(SummarizeResponseDTO.class);
    }



    /**
     * 프론트에서 받은 userInputText, userInputFile을
     * Python LLM 서버(http://localhost:8000)로 전송하는 APi
     */
    @PostMapping(value = "/send", consumes = "multipart/form-data")
    public ResponseEntity<SuccessResponse<String>> sendToLLM(
            @RequestParam(value = "userInputText", required = false) String userInputText,
            @RequestParam(value = "userInputFile", required = false) List<MultipartFile> userInputFiles
    ) throws IOException {

        String llmResponse = llmClient.sendMultipartToLLM(userInputText, userInputFiles);

        return ResponseEntity.ok(
            new SuccessResponse<>(true, llmResponse, "요청 성공")
        );

    }



}
