package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.*;

import java.util.Map;

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

    private Integer sessionId;
    private Integer chatMessageId;
    private String question;
    private String answer;

}