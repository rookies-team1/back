package com.alreadyemployee.alreadyemployee.news.service;


import com.alreadyemployee.alreadyemployee.news.controller.dto.AddNewsRequestDTO;
import com.alreadyemployee.alreadyemployee.news.controller.dto.NewsDetailResponseDTO;
import com.alreadyemployee.alreadyemployee.news.controller.dto.NewsSimpleResponseDTO;
import com.alreadyemployee.alreadyemployee.news.entity.News;
import com.alreadyemployee.alreadyemployee.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class NewsService {
    private final NewsRepository newsRepository;

    public void addNews(AddNewsRequestDTO data) {
        News news = News.builder()
                .companyName(data.getCompanyName())
                .title(data.getTitle())
                .contents(data.getContents())
                .publishDate(data.getPublishDate()) // 날짜만 왔다면 시간 채우기
                .build();

        newsRepository.save(news);
    }

    public List<String> getAllCompanyNames() {
        return newsRepository.findAll()
                .stream()
                .map(news -> news.getCompanyName())
                .distinct()
                .toList();
    }

    public List<NewsSimpleResponseDTO> getAllNewsTitles() {
        return newsRepository.findAll()
                .stream()
                .map(news -> NewsSimpleResponseDTO.builder()
                        .id(news.getId())
                        .title(news.getTitle())
                        .build())
                .toList();
    }

    public String getNewsTitleById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("뉴스를 찾을 수 없습니다."))
                .getTitle();
    }

    public NewsDetailResponseDTO getNewsDetailById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("뉴스를 찾을 수 없습니다."));
        return NewsDetailResponseDTO.builder()
                .title(news.getTitle())
                .contents(news.getContents())
                .build();
    }

}
