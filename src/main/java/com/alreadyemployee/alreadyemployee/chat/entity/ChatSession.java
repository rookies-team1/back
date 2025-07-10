package com.alreadyemployee.alreadyemployee.chat.entity;

import com.alreadyemployee.alreadyemployee.news.entity.News;
import com.alreadyemployee.alreadyemployee.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 채팅 세션 엔티티 클래스
 * 사용자와 뉴스 간의 채팅 세션을 표현
 */
@Entity
@Table(name = "chat_session")
@Getter @Setter @Builder
public class ChatSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 세션 ID

    // 이 채팅 세션은 어떤 뉴스에 대한 것인가?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;  // 어떤 뉴스인지

    // 어떤 사용자가 시작한 채팅인가?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 어떤 사용자인지

    // 메시지들
    @OneToMany(mappedBy = "chatSession", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>(); // 세션에 속한 메시지 목록

    private LocalDateTime createdAt;    // 세션 생성 시간

    /**
    * 엔티티 생성 시 자동으로 현재 시간을 설정
    */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}