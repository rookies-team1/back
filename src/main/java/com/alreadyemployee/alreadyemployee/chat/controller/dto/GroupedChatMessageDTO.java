package com.alreadyemployee.alreadyemployee.chat.controller.dto;

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
    private SimpleMessageDTO question;
    private SimpleMessageDTO answer;
}