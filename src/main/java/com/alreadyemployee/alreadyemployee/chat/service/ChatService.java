package com.alreadyemployee.alreadyemployee.chat.service;

import com.alreadyemployee.alreadyemployee.chat.controller.dto.ChatProxyRequestDTO;
import com.alreadyemployee.alreadyemployee.chat.controller.dto.ChatResponseDTO;
import com.alreadyemployee.alreadyemployee.chat.controller.dto.HistoryMessageDTO;
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
import com.alreadyemployee.alreadyemployee.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final ChatProxyService chatProxyService;

    public NewsByIdDTO getNewsById(Long newsId){
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NEWS_NOT_FOUND));

        return NewsByIdDTO.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContents())
                .company_name(news.getCompanyName())
                .build();
    }

    @Transactional
    public String handleChat(Long userId, Long newsId, String question, MultipartFile file) {
//        user 찾기
        User user = userRepository.findById(userId).orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
//       news 찾기
        News news = newsRepository.findById(newsId).orElseThrow(()->new BusinessException(ErrorCode.NEWS_NOT_FOUND));

//        user에 해당하는 newsId를 찾고 없으면 ChatSession 생성
        ChatSession session = chatSessionRepository.findByUserIdAndNewsId(userId, newsId)
                .orElseGet(() -> chatSessionRepository.save(
                        ChatSession.builder()
                                .user(user)
                                .news(news)
                                .messages(new ArrayList<>()) // ✅ null 방지
                                .build()
                ));

        // 질문 저장
        ChatMessage questionMsg = chatMessageRepository.save(ChatMessage.builder()
                .chatSession(session)
                .type(ChatType.human)
                .content(question)
                .build());

        // chat_history 구성
        List<HistoryMessageDTO> history = session.getMessages().stream()
                .map(msg -> new HistoryMessageDTO(msg.getType().name().toLowerCase(), msg.getContent()))
                .toList();

        // 파이썬 요청 DTO 구성
        ChatProxyRequestDTO requestDTO = new ChatProxyRequestDTO(
                session.getId(),
                userId,
                question,
                questionMsg.getId(),
                newsId,
                news.getCompanyName(),
                history
        );

        // 파이썬 서버 호출
        ChatResponseDTO response = chatProxyService.sendToPython(requestDTO, file);

        // 답변 저장 (groupId = 질문 메시지 ID)
        chatMessageRepository.save(ChatMessage.builder()
                .chatSession(session)
                .type(ChatType.ai)
                .content(response.answer())
                .groupId(questionMsg.getId())
                .build());

        return response.answer();
    }
}