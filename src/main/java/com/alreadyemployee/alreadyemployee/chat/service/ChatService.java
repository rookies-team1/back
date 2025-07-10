package com.alreadyemployee.alreadyemployee.chat.service;

import com.alreadyemployee.alreadyemployee.chat.controller.dto.NewsByIdDTO;
import com.alreadyemployee.alreadyemployee.chat.dto.ChatMessageResponseDTO;
import com.alreadyemployee.alreadyemployee.chat.dto.ChatType;
import com.alreadyemployee.alreadyemployee.chat.entity.ChatMessage;
import com.alreadyemployee.alreadyemployee.chat.entity.ChatSession;
import com.alreadyemployee.alreadyemployee.chat.repository.ChatMessageRepository;
import com.alreadyemployee.alreadyemployee.chat.repository.ChatSessionRepository;
import com.alreadyemployee.alreadyemployee.exception.BusinessException;
import com.alreadyemployee.alreadyemployee.exception.ErrorCode;
import com.alreadyemployee.alreadyemployee.news.entity.News;
import com.alreadyemployee.alreadyemployee.news.repository.NewsRepository;
import com.alreadyemployee.alreadyemployee.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final NewsRepository newsRepository;

    public NewsByIdDTO getNewsById(Long newsId){
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NEWS_NOT_FOUND));
        return NewsByIdDTO.builder()
                .id(news.getId())
                .title(news.getTitle())
                .contents(news.getContents())
                .comapnyName(news.getCompanyName())
                .build();
    }

    /**
     * 채팅 세션을 저장하고 사용자 메시지를 저장한 후, Python LLM으로 전송하고,
     * 응답을 받아 저장하는 전체 흐름을 처리
     */
    //해당 어노테이션 덕분에 handleChat 메서드의 모든 저장 로직은 중간에 오류나면 rollback
    @Transactional
    public ChatMessageResponseDTO handleChat(
            User user, News news, String userInputText, String llmResponseText
    ) {

        // 1. 세션 생성 및 저장
        //ChatSession 클래스를 사용한 인스턴스에 .builder() ... .build()로 값을 지정하고 생성
        ChatSession chatSession = ChatSession.builder()
                .user(user)
                .news(news)
                .build();
        // 이후 chat_session 테이블에 값 저장
        chatSession = chatSessionRepository.save(chatSession);

        // 2. 사용자 입력 메시지 저장
        // 마찬가지로 ChatMessage 클래스를 사용한 인스턴스에 .builder() ... .build()로 값을 지정하고 저장
        ChatMessage userMessage = ChatMessage.builder()
                .chatSession(chatSession)
                .type(ChatType.human)
                .content(userInputText)
                .build();
        // 이후 chat_message 테이블에 저장
        chatMessageRepository.save(userMessage);

        // 3. python llm 모델의 응답 메시지 저장
        // ChatMessage 클래스를 사용한 인스턴스에 .builder() ... .build()로 값을 지정하고 저장
        ChatMessage llmMessage = ChatMessage.builder()
                .chatSession(chatSession)
                .type(ChatType.ai)
                .content(llmResponseText)
                .build();
        // 이후 chat_message 테이블에 저장
        chatMessageRepository.save(llmMessage);

        // 4. 응답 반환
        // 질문과 답변만 반환
        return ChatMessageResponseDTO.builder()
                .question(userInputText)
                .answer(llmResponseText)
                .build();
    }
}
