package com.alreadyemployee.alreadyemployee.news.service;


import com.alreadyemployee.alreadyemployee.news.controller.dto.AddNewsRequestDTO;
import com.alreadyemployee.alreadyemployee.news.entity.News;
import com.alreadyemployee.alreadyemployee.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
