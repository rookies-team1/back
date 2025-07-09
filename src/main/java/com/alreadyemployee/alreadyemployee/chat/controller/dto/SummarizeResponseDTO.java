package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.*;

/**
 * 뉴스 요약 결과를 담는 DTO 클래스
 * 외부 요약 API로부터 받은 응답을 매핑
 */
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummarizeResponseDTO {
    private String summary; //요약 된 내용
    private boolean isError;  //에러 발생 여부
    private String error_content;   //에러 발생 시 에러 내용
    
}