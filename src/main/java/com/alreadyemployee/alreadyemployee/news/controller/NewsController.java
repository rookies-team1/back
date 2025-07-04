package com.alreadyemployee.alreadyemployee.news.controller;


import com.alreadyemployee.alreadyemployee.news.controller.dto.AddNewsRequestDTO;
import com.alreadyemployee.alreadyemployee.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;


    @PostMapping("/add")
    public ResponseEntity<Void> addNews(@RequestBody AddNewsRequestDTO data){
        newsService.addNews(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
