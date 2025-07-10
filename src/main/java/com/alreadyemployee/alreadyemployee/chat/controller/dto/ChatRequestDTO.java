package com.alreadyemployee.alreadyemployee.chat.controller.dto;

import lombok.*;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

/**
 * main.py:  AI 기반 뉴스 질의응답 및 요약 기능을 제공하는 FastAPI 백엔드 서버
 * main.py 안의 클래스 중 하나인 ChatRequest 클래스
 *
 */
@Getter @Setter @Builder
public class ChatRequestDTO {

    //back
    private Integer sessionId; //ChatSession의 기본키는 Long 타입 변수
    private Integer userId; //User의 기본키는 Long 타입 변수
    private Integer chatMessageId;
    private Integer newsId; // 뉴스 ID (Integer로 변경)
    private String company; //News의 companyName은 String 형 변수
    private List<HistoryMessageDTO> chatHistory;

    //front
    private Optional<MultipartFile> file; // 사용자가 넘겨주는 파일(있을수도 없을수도 있음)
    // front: 사용자가 front에서 넘겨 준 질문
    // back: front에서 넘겨받은 질문
    private String question;

}
