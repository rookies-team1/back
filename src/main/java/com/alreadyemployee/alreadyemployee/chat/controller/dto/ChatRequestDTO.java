package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

/**
 * main.py:  AI 기반 뉴스 질의응답 및 요약 기능을 제공하는 FastAPI 백엔드 서버
 * main.py 안의 클래스 중 하나인 ChatRequest 클래스
 */
@Getter
@Setter
@Builder
public class ChatRequestDTO {

    //back
    private Integer sessionId; //ChatSession의 기본키는 Long 타입 변수
    private Integer userId; //User의 기본키는 Long 타입 변수
    private Integer chatMessageId;
    private Integer newsId; // 뉴스 ID (Integer로 변경)
    private String company; //News의 companyName은 String 형 변수
    private List<HistoryMessageDTO> chatHistory;

    //front
    private Optional<MultipartFile> file; // 사용자가 넘겨주는 파일(있을수도 없을수도 있음)
    // front: 사용자가 front에서 넘겨 준 질문
    // back: front에서 넘겨받은 질문
    private String question;

    /**
     * HTTP POST 요청에서 body로 들어오는 snake_case 형식의 데이터를 camelCase 형식인 데이터로 변환해주는 메서드
     */
    //front -> llm server
    public static ChatRequestDTO llmSnakeToCamel(Map<String, Object> snakeMap) {
        return ChatRequestDTO.builder()
                .sessionId((Integer) snakeMap.get("session_id"))
                .userId((Integer) snakeMap.get("user_id"))
                .chatMessageId((Integer) snakeMap.get("chat_message_id"))
                .newsId((Integer) snakeMap.get("news_id"))
                .company((String) snakeMap.get("company"))
                .question((String) snakeMap.get("question"))
                .build();
    }
    //llm server -> front
    public static ChatRequestDTO frontSnakeToCamel(Map<String, Object> snakeMap) {
        return ChatRequestDTO.builder()
                .question((String) snakeMap.get("question"))
                .file((Optional<MultipartFile>) snakeMap.get("file"))
                .build();
    }


}
