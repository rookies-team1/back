package com.alreadyemployee.alreadyemployee.chat.service;

import com.alreadyemployee.alreadyemployee.chat.controller.dto.NewsByIdDTO;
import com.alreadyemployee.alreadyemployee.chat.controller.dto.SummarizeRequestDTO;
import com.alreadyemployee.alreadyemployee.chat.controller.dto.SummarizeResponseDTO;
import com.alreadyemployee.alreadyemployee.chat.repository.ChatMessageRepository;
import com.alreadyemployee.alreadyemployee.chat.repository.ChatSessionRepository;
import com.alreadyemployee.alreadyemployee.exception.BusinessException;
import com.alreadyemployee.alreadyemployee.exception.ErrorCode;
import com.alreadyemployee.alreadyemployee.news.entity.News;
import com.alreadyemployee.alreadyemployee.news.repository.NewsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;


@Service
@AllArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final NewsRepository newsRepository;
    private final RestClient restClient;  // 주입받기

    public NewsByIdDTO getNewsById(Long newsId){
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NEWS_NOT_FOUND));
        return NewsByIdDTO.builder()
                .id(news.getId())
                .title(news.getTitle())
                .contents(news.getContents())
                .companyName(news.getCompanyName())
                .build();
    }

    /**
     * 뉴스 정보를 요약하기 위해 외부 요약 서버(summarizer API)로 요청을 보내고, 요약 결과를 반환하는 메서드
     */
    public String summarizeNews(NewsByIdDTO newsDto) {
        // 요약 요청에 필요한 DTO 생성
        SummarizeRequestDTO requestDto = SummarizeRequestDTO.builder()
                .id(newsDto.getId())
                .title(newsDto.getTitle())
                .content(newsDto.getContents())
                .companyName(newsDto.getCompanyName())
                .build();

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(requestDto);
            System.out.println("보내는 JSON: " + json);

            // RestClient를 사용해 summarizer 서버에 POST 요청
            SummarizeResponseDTO response = restClient.post()
                    .uri("/summarize")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromValue(requestDto))
                    .retrieve()
                    .body(SummarizeResponseDTO.class);


            // 응답이 null이거나 에러 플래그가 true인 경우, 사용자 정의 예외 발생
            if (response == null || response.isError()) {
                throw new BusinessException(ErrorCode.SUMMARIZATION_FAILED);
            }

            // 요약 결과 반환
            return response.getSummary();

        } catch (Exception e) {
            // 외부 API 호출 중 예외 발생 시 사용자 정의 예외로 변환하여 처리
            throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR, e.getMessage());
        }
    }
}
