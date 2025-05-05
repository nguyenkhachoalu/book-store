package com.project_sem4.book_store.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "chat_room_id", nullable = false)
    UUID chatRoomId;

    @Column(name = "sender_id", nullable = false)
    UUID senderId;

    @Column(nullable = false, columnDefinition = "TEXT")
    String message;

    @Column(name = "message_type", nullable = false, length = 50)
    String messageType = "TEXT";

    @Column(name = "reply_to_message_id")
    UUID replyToMessageId;

    @Column(name = "is_seen")
    Boolean isSeen = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}