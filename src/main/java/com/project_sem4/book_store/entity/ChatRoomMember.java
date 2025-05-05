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
@Table(name = "chat_room_members")
@IdClass(ChatRoomMemberId.class)
public class ChatRoomMember {
    @Id
    UUID chatRoomId;

    @Id
    UUID userId;

    @Column(nullable = false, length = 50)
    String role = "MEMBER";

    @Column(name = "joined_at")
    LocalDateTime joinedAt;
}
