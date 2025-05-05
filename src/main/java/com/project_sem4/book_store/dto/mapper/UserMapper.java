package com.project_sem4.book_store.dto.mapper;

import com.project_sem4.book_store.dto.request.user_request.UserCreateRequest;
import com.project_sem4.book_store.dto.response.UserResponse;
import com.project_sem4.book_store.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);
    UserResponse toUserResponse(User user);
}
