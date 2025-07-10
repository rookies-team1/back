package com.alreadyemployee.alreadyemployee.chat.entity;

import com.alreadyemployee.alreadyemployee.news.entity.News;
import com.alreadyemployee.alreadyemployee.user.entity.User;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 채팅 세션 단위를 표현한느 엔티티 클래스
 * 하나의 User가 하나의 News에 대한 채팅을 시작할 때 하나의 세션이 만들어짐
 */
@Entity
@Table(name = "chat_session")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class ChatSession {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //PK, 값 자동 증가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //FK, User 클래스에 대한 LAZY 타입의 외래키 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news; //FK, News 클래스에 대한 LAZY 타입의 외래키 설정

    private LocalDateTime createdAt; //생성 시간 (밑의 @PrePersist로 자동 설정)

    @OneToMany(mappedBy = "chatSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>(); //해당 세션에 연결 된 전체 메세지 목록

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

