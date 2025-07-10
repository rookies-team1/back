package com.alreadyemployee.alreadyemployee.chat.entity;

import com.alreadyemployee.alreadyemployee.chat.dto.ChatType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 채팅 메시지 엔티티 클래스
 * 채팅 세션 내의 개별 메시지를 데이터베이스에 저장하기 위한 구조
 */
@Entity
@Table(name = "chat_message")
@Getter @Setter @Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 메시지 ID

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;  // 실제 메시지 내용
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_session_id")
    private ChatSession chatSession;    // 메세지가 속한 채팅 세션

    @Enumerated(EnumType.STRING)
    private ChatType type; // human(질문) / ai(답변)


    private LocalDateTime createdAt;    // 메세지 생성 시간

    /**
     * 엔티티 생성 시 자동으로 현재 시간을 설정
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}