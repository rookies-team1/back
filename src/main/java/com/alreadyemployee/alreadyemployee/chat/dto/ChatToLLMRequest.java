package com.alreadyemployee.alreadyemployee.chat.dto;

import lombok.*;

import java.util.List;

/**
 * LLM 서버로 전송하는 요청 데이터를 담는 DTO 클래스
 * - 질문(question)과 이전 대화 기록(chatHistory)을 포함
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder
public class ChatToLLMRequest {
    private String question; //현재 질문
    private List<String> chatHistory; //이전 대화 목록(질문 및 답변 텍스트만 포함)
}