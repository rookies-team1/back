package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.*;

/**
 * 뉴스 ID로 조회한 뉴스 정보를 담는 DTO 클래스
 */
@Getter @Setter @Builder
public class NewsByIdDTO {
    private Long id;          // 뉴스 ID
    private String title;     // 뉴스 제목
    private String contents;  // 뉴스 내용
    private String companyName; // 회사명
}