package com.alreadyemployee.alreadyemployee.chat.controller;

import com.alreadyemployee.alreadyemployee.chat.controller.dto.NewsByIdDTO;
import com.alreadyemployee.alreadyemployee.chat.service.ChatService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;

    //지금 이건 안돼요
    @PostMapping("/summarize/{newsId}")
    public String summarize(@PathVariable Long newsId) {
        //DB에서 뉴스 데이터 가져오기
        NewsByIdDTO newsById = chatService.getNewsById(newsId);

        //파이썬 서버로 요청 보내기
        String summary = chatService.summarizeNews(newsById);

        //파이썬 서버에서 요약 결과를 받아 클라이언트로 응답
        return summary;
    }



}
