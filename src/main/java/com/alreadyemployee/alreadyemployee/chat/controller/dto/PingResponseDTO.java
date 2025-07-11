package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PingResponseDTO {
    private String message;
}
