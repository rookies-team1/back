package com.alreadyemployee.alreadyemployee.chat.entity;

import com.alreadyemployee.alreadyemployee.chat.dto.ChatType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 실제 주고 받는 채팅 메세지를 저장하는 엔티티 클래스
 */
@Entity
@Table(name = "chat_message")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class ChatMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //PK, 값 자동 증가

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; //실제 메세지 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_session_id")
    private ChatSession chatSession; //어떠한 세션에 속한 메세지인지 지연 로딩으로 연결

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private ChatType type; // human(질문) / ai(답변)

}
