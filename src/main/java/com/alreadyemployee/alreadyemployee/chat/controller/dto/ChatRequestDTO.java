package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * 채팅 요청 정보를 담는 DTO 클래스
 * 클라이언트에서 채팅 관련 요청을 보낼 때 사용하는 데이터 구조
 */
@Getter @Setter
public class ChatRequestDTO {
    private int sessionId;       // 채팅 세션 ID
    private int userId;          // 사용자 ID
    private String question;     // 사용자가 입력한 질문
    private int chatMessageId;   // 메시지 ID
    private int newsId;          // 관련 뉴스 ID
    private String company;      // 회사명
    private List<HistoryMessageDTO> chatHistory; // 이전 채팅 내역
    /**
     * 채팅 내역의 개별 메시지를 나타내는 내부 클래스
     */
    @Getter @Setter
    public static class HistoryMessageDTO {
        private String type;     // 메시지 타입 ("hunam", "ai")
        private String content;  // 메시지 내용
    }
}