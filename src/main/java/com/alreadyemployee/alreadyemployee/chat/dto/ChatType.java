package com.alreadyemployee.alreadyemployee.chat.dto;

/**
 * 채팅 메세지의 타입을 정의하는 Enum
 * - human: 사용자 질문
 * - ai: LLM 모델 응답
 */
public enum ChatType {
    human, ai;

    public boolean isHuman() {
        return this == human;
    }

    public boolean isAI() {
        return this == ai;
    }
}