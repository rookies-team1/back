package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.*;

/**
 * main.py:  AI 기반 뉴스 질의응답 및 요약 기능을 제공하는 FastAPI 백엔드 서버
 * main.py 안의 클래스 중 하나인 HistoryMessage 클래스
 * class HistoryMessage(BaseModel):
 *     type: str
 *     content: str
 */
@Getter @Setter @Builder
public class HistoryMessageDTO {
    private String type;
    private String content;
}
