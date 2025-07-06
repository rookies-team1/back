package com.alreadyemployee.alreadyemployee.exception.global;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private boolean success;
    private String errorCode;
    private String errorMessage;
}
