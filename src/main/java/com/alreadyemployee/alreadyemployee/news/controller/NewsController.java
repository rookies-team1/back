package com.alreadyemployee.alreadyemployee.news.controller;


import com.alreadyemployee.alreadyemployee.news.controller.dto.*;
import com.alreadyemployee.alreadyemployee.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    //하나의 뉴스 데이터 삽입
    @PostMapping("/add")
    public ResponseEntity<Void> addNews(@RequestBody AddNewsRequestDTO data) {
        newsService.addNews(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //여러개의 뉴스 데이터 삽입
    @PostMapping("/adds")
    public ResponseEntity<Void> addsNews(@RequestBody List<AddNewsRequestDTO> newsList) {
        for (AddNewsRequestDTO dto : newsList) {
            newsService.addNews(dto);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /* 메인 페이지 */
    //기업 이름 카테고리 리스트 반환
    @GetMapping("/companies")
    public ResponseEntity<List<String>> getCompanyNames() {
        return ResponseEntity.ok(newsService.getAllCompanyNames());
    }

    //뉴스 제목 리스트 반환
    @GetMapping("/titles")
    public ResponseEntity<List<NewsSimpleResponseDTO>> getNewsTitles() {
        return ResponseEntity.ok(newsService.getAllNewsTitles());
    }

    /* 뉴스 상세 페이지 */
    //뉴스 제목 반환
    @GetMapping("/{id}/title")
    public ResponseEntity<String> getNewsTitleById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsTitleById(id));
    }

    //뉴스 전문 반환
    @GetMapping("/{id}/detail")
    public ResponseEntity<NewsDetailResponseDTO> getNewsDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsDetailById(id));
    }

    @GetMapping("/{company}")
    public ResponseEntity<List<NewsListByCompanyResponseDTO>> getNewsListByCompany(@PathVariable String company) {
        return ResponseEntity.ok(newsService.getNewsListByCompany(company));
    }

    // id와 contents를 포함한 모든 뉴스 반환
    @GetMapping("/all-id-content")
    public ResponseEntity<List<NewsIdContentResponseDTO>> getAllNewsIdContent() {
        return ResponseEntity.ok(newsService.getAllNewsIdContent());
    }

}