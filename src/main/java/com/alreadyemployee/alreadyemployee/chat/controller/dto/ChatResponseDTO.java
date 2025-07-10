package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.*;
/**
 * main.py:  AI 기반 뉴스 질의응답 및 요약 기능을 제공하는 FastAPI 백엔드 서버
 * main.py 안의 클래스 중 하나인 ChatResponse 클래스
 * class ChatResponse(BaseModel):
 *     session_id: int
 *     chat_message_id: int
 *     question: str
 *     answer: str
 */
@Getter @Setter @Builder
public class ChatResponseDTO {
    // Python의 session_id -> Java의 sessionId (ObjectMapper가 snake_case -> camelCase 변환)
    private Integer sessionId;
    // Python의 chat_message_id -> Java의 chatMessageId
    private Integer chatMessageId;
    // Python의 question -> Java의 question
    private String question;
    // Python의 answer -> Java의 answer
    private String answer;
}