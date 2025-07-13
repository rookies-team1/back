package com.alreadyemployee.alreadyemployee.chat.repository;

import com.alreadyemployee.alreadyemployee.chat.entity.ChatMessage;
import com.alreadyemployee.alreadyemployee.chat.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatSession(ChatSession chatSession);
}
