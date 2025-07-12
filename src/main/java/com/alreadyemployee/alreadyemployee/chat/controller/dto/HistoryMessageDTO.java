package com.alreadyemployee.alreadyemployee.chat.controller.dto;

public record HistoryMessageDTO(
        String type,    // "human" / "ai"
        String content
) {}
