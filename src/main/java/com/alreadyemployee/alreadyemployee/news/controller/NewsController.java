package com.alreadyemployee.alreadyemployee.news.controller;


import com.alreadyemployee.alreadyemployee.news.controller.dto.AddNewsRequestDTO;
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
    public ResponseEntity<Void> addNews(@RequestBody AddNewsRequestDTO data){
        newsService.addNews(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    //여러개의 뉴스 데이터 삽입
    @PostMapping("/adds")
    public ResponseEntity<Void> addsNews(@RequestBody List<AddNewsRequestDTO> newsList){
        for(AddNewsRequestDTO dto : newsList) {
            newsService.addNews(dto);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
