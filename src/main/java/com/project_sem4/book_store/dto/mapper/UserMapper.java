package com.project_sem4.book_store.dto.mapper;

import com.project_sem4.book_store.dto.request.user_request.UserCreateRequest;
import com.project_sem4.book_store.dto.response.data_response_user.UserResponse;
import com.project_sem4.book_store.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    User toUser(UserCreateRequest request);
    @Mapping(target = "roles", source = "roles")
    UserResponse toUserResponse(User user);
}
