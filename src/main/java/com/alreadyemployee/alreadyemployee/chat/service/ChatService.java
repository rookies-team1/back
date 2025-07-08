package com.alreadyemployee.alreadyemployee.chat.service;

import com.alreadyemployee.alreadyemployee.chat.controller.dto.NewsByIdDTO;
import com.alreadyemployee.alreadyemployee.chat.repository.ChatMessageRepository;
import com.alreadyemployee.alreadyemployee.chat.repository.ChatSessionRepository;
import com.alreadyemployee.alreadyemployee.exception.BusinessException;
import com.alreadyemployee.alreadyemployee.exception.ErrorCode;
import com.alreadyemployee.alreadyemployee.news.entity.News;
import com.alreadyemployee.alreadyemployee.news.repository.NewsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionRepository chatSessionRepository;
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
}
