package com.alreadyemployee.alreadyemployee.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupedChatMessageDTO {
    private Long groupId;
    private ChatMessageResponseDTO question;
    private ChatMessageResponseDTO answer;
}
