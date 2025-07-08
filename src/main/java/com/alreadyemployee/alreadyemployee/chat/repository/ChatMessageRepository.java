package com.alreadyemployee.alreadyemployee.chat.repository;


import com.alreadyemployee.alreadyemployee.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}
