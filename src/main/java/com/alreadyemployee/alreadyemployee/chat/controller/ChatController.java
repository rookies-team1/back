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

    @PostMapping("/summarize/{newsId}")
    public void summarize(@PathVariable Long newsId){
        NewsByIdDTO newsById=chatService.getNewsById(newsId);

        //파이썬으로 요청 넘겨주기

        //요청 받아서 클라이언트로 반환

    }

}
