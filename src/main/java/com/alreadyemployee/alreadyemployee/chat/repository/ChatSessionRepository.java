package com.alreadyemployee.alreadyemployee.chat.repository;

import com.alreadyemployee.alreadyemployee.chat.entity.ChatSession;
import com.alreadyemployee.alreadyemployee.news.entity.News;
import com.alreadyemployee.alreadyemployee.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    // 사용자와 뉴스를 기반으로 채팅 세션을 찾는 메서드
    Optional<ChatSession> findByUserAndNews(User user, News news);
    Optional <ChatSession> findByUserIdAndNewsId(Long userId,Long newsId);
}