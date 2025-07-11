package com.alreadyemployee.alreadyemployee.chat.controller.dto;

public record HistoryMessageDTO(
        String role,    // "human" / "ai"
        String content
) {}
