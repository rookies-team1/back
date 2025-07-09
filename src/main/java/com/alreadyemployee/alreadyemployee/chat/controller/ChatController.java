package com.alreadyemployee.alreadyemployee.chat.controller;


import com.alreadyemployee.alreadyemployee.chat.dto.ChatMessageRequestDTO;
import com.alreadyemployee.alreadyemployee.chat.dto.ChatMessageResponseDTO;
import com.alreadyemployee.alreadyemployee.chat.service.LLMClient;
import com.alreadyemployee.alreadyemployee.exception.global.SuccessResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    /**
     * 테스트용 코드
     * 채팅 메시지 요청을 처리하는 테스트용 POST 엔드포인트
     * - 다양한 파라미터를 받아 DTO 형태로 매핑 테스트
     */
    @PostMapping(value = "/test", consumes = "multipart/form-data")
    public ResponseEntity<ChatMessageResponseDTO> testChatDTO (
            //@RequestParam은 Http Post 요청 할 때 body에 들어가는 값
            @RequestParam("sessionId") Long sessionId,      //채팅 세션 id
            @RequestParam("userId") Long userId,            //사용자 id
            //required = false: 값이 없어도 가능 없으면 null값 바인딩
            @RequestParam(value = "userInputText", required = false) String userInputText,  //사용자 입력 텍스트
            @RequestParam(value = "userInputFile", required = false) MultipartFile userInputFile,   //사용자 입력 파일
            @RequestParam("chatMessageId") Long chatMessageId,  //채팅 메세지 id
            @RequestParam("newsId") Long newsId,                //뉴스 id
            @RequestParam("companyName") String companyName,    //뉴스에 나온 회사 이름
            @RequestParam(value = "chatHistories", required = false) String chatHistoriesJson //이전 대화 내역을 담은 JSON 문자열
        ) throws IOException {

        // chatHistoriesJson 문자열을 JSON → List<ChatHistory>로 변환
        List<ChatMessageRequestDTO.ChatHistory> chatHistories = null;
        if(chatHistoriesJson != null) {
            ObjectMapper mapper = new ObjectMapper();
            chatHistories = mapper.readValue(chatHistoriesJson,
                    new TypeReference<List<ChatMessageRequestDTO.ChatHistory>>() {});
        }

        // 테스트용 응답 데이터 생성
        ChatMessageResponseDTO response = ChatMessageResponseDTO.builder()
                .sessionId(sessionId)
                .chatMessageId(chatMessageId)
                .question(userInputText != null ? userInputText : (userInputFile != null ? "파일 업로드 성공" : ""))
                .answer("테스트 성공") //실제 LLM 응답이 아닌 임스 텍스트
                .build();

        return ResponseEntity.ok(response);
    }




    private final LLMClient llmClient;
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
