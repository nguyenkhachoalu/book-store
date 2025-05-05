package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {}
