package com.alreadyemployee.alreadyemployee.chat.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 프론트에서 채팅 요청을 보낼 때 사용하는 DTO 클래스
 * - 세션 id, 유저 id, 사용자 입력 텍스트, 사용자 입력 파일,
 *      채팅 메세지 id, 뉴스 id, 뉴스 회사 이름을 포함
 * - 이전 대화 기록을 List로 함께 전송
 */
@NoArgsConstructor  //매개 변수 없는 생성자 자동 생성
@AllArgsConstructor //모든 변수가 매개 변수인 생성자 자동 생성
@Getter @Setter @Builder
public class ChatMessageRequestDTO {

    private Long sessionId; //세션 id
    private Long userId;    //유저 id
    //유저 입력
    private String userInputText;   //유저 입력(String 텍스트)
    private MultipartFile userInputFile; //유저 입력(Multipart Form Data 파일)

    private Long chatMessageId; //채팅 메세지 id
    private Long newsId; //뉴스 id
    private String companyName; //뉴스 회사 이름

    private List<ChatMessageRequestDTO.ChatHistory> chatHistories; //이전 대화 목록

    /**
     * 과거 채팅 기록을 나타내는 내부 static 클래스
     * - ChatType(human 또는 ai)과 content(내용)으로 구성됨
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter @Builder
    public static class ChatHistory{
        //Enum으로 구현
        private ChatType type;  //human=사용자 질문, ai=LLM 모델 답변
        private String content; //실제 대화 내용
    }

}
