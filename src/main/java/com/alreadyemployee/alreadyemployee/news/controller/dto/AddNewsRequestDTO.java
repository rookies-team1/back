package com.alreadyemployee.alreadyemployee.news.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddNewsRequestDTO {
    @NotBlank(message = "회사 이름을 입력하세요.")
    private String companyName;

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String contents;

    @NotBlank(message = "발행 날짜를 입력하세요.")
    private LocalDate publishDate;
}
