package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.*;

/**
 * 뉴스 요약 요청에 필요한 데이터를 담는 DTO 클래스
 * 외부 요약 API로 전송되는 요청 형식
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class SummarizeRequestDTO {
    private Long id;           // 뉴스 ID
    private String title;      // 뉴스 제목
    private String content;    // 요약할 뉴스 내용
    private String companyName; // 회사명
}