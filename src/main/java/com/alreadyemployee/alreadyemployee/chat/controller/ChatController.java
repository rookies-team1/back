package com.alreadyemployee.alreadyemployee.chat.controller;


import com.alreadyemployee.alreadyemployee.chat.controller.dto.*;
import com.alreadyemployee.alreadyemployee.chat.service.ChatProxyService;
import com.alreadyemployee.alreadyemployee.chat.service.ChatService;
import com.alreadyemployee.alreadyemployee.user.entity.CustomUserDetails;

import com.alreadyemployee.alreadyemployee.exception.global.SuccessResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final RestClient restClient;
    private final ChatService chatService;

    private final ChatProxyService chatProxyService;

    @GetMapping("/ping")
    public PingResponseDTO ping(){
        return restClient.get().uri("/").retrieve().body(PingResponseDTO.class);
    }


    @PostMapping("/summarize/{newsId}")
    public SummarizeResponseDTO summarize(@PathVariable Long newsId){
        NewsByIdDTO newsById=chatService.getNewsById(newsId);
        System.out.println(newsById.getCompany_name());
//        파이썬으로 요청 넘겨주기
        return restClient.post().uri("/summarize").body(newsById).retrieve().body(SummarizeResponseDTO.class);
    }
    /**
     * 클라이언트로부터 채팅 요청(JSON, 파일)을 받아 처리하고 응답합니다.
//   * @param chatRequestDTO 채팅 요청 데이터 (질문, 파일, 뉴스 ID 등)
     * @param userDetails 현재 인증된 사용자 정보 (Spring Security가 주입)
     * @return 처리 결과가 담긴 ChatResponseDTO
     */
    @PostMapping(value = "/ask", consumes = {"multipart/form-data"})
    public ResponseEntity<String> ask(
            @RequestPart("question") String question,
            @RequestPart("newsId") Long newsId,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String answer = chatService.handleChat(userDetails.getId(), newsId, question, file);
        return ResponseEntity.ok(answer);
    }
}