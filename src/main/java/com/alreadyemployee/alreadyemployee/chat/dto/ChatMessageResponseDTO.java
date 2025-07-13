package com.alreadyemployee.alreadyemployee.chat.dto;

import com.alreadyemployee.alreadyemployee.chat.entity.ChatMessage;
import lombok.*;

/**
 * 클라이언트에 응답으로 전달되는 채팅 메시지 DTO
 * - 질문과 답변, 세션 ID, 메시지 ID를 포함함
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class ChatMessageResponseDTO {
    private Long sessionId; //채팅 세션 id
    private Long chatMessageId; //채팅 메세지 id
    private String question; //사용자의 질문
    private String answer; //python LLM 모델의 답변
//    private LocalDateTime timeStamp;    //필요할 때 넣는 시간 기록

    public static ChatMessageResponseDTO fromEntity(ChatMessage message) {
        return ChatMessageResponseDTO.builder()
                .sessionId(message.getChatSession().getId())
                .chatMessageId(message.getId())
                .question(message.getType().isHuman() ? message.getContent() : null)
                .answer(message.getType().isAI() ? message.getContent() : null)
                .build();
    }

    public static ChatMessageResponseDTO fromEntity(ChatMessage humanMessage, ChatMessage aiMessage) {
        return ChatMessageResponseDTO.builder()
                .sessionId(humanMessage.getChatSession().getId())
                .chatMessageId(humanMessage.getId())  // 질문 기준으로 ID 사용 (원하면 groupId로 교체 가능)
                .question(humanMessage.getContent())
                .answer(aiMessage != null ? aiMessage.getContent() : null)
                .build();
    }
}