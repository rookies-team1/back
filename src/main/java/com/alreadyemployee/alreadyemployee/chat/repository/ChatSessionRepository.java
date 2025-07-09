package com.alreadyemployee.alreadyemployee.chat.repository;

import com.alreadyemployee.alreadyemployee.chat.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
}