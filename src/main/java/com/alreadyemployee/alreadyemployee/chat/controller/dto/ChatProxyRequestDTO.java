package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import java.util.List;

public record ChatProxyRequestDTO(
        Long session_id,
        Long user_id,
        String question,
        Long chat_message_id,
        Long news_id,
        String company,
        List<HistoryMessageDTO> chat_history
) {}
