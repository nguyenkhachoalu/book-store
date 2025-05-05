package com.project_sem4.book_store.repository;

import com.project_sem4.book_store.entity.ChatRoomMember;
import com.project_sem4.book_store.entity.ChatRoomMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, ChatRoomMemberId> {
}
