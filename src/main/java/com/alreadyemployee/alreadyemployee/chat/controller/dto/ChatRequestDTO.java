package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class ChatRequestDTO {
    private int sessionId;
    private int userId;
    private String question;
    private int chatMessageId;
    private int newsId;
    private String company;
    private List<HistoryMessageDTO> chatHistory;

    @Getter @Setter
    public static class HistoryMessageDTO {
        private String type;
        private String content;
    }
}