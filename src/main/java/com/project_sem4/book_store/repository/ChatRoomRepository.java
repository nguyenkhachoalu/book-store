package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {}