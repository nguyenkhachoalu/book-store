package com.project_sem4.book_store.controller;


import com.project_sem4.book_store.dto.request.user_request.ChangePasswordRequest;
import com.project_sem4.book_store.dto.request.user_request.UpdateUserRoleRequest;
import com.project_sem4.book_store.dto.request.user_request.UserUpdateRequest;
import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_user.UserResponse;
import com.project_sem4.book_store.enum_type.UserSearchType;
import com.project_sem4.book_store.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ApiResponse<PagedResponse<UserResponse>> getUsers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "ALL") UserSearchType type,
            Pageable pageable
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<PagedResponse<UserResponse>>builder()
                .result(userService.getPagedUsers(keyword, type, pageable))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable UUID userId,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
    @PutMapping("/{userId}/change-password")
    public ApiResponse<UserResponse> changePassword(
            @PathVariable UUID userId,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.changePassword(userId, request))
                .build();
    }

    @PutMapping("/{userId}/roles")
    public ApiResponse<String> updateRoles(
            @PathVariable UUID userId,
            @RequestBody UpdateUserRoleRequest request
            ) {
        return ApiResponse.<String>builder()
                .result(userService.updateUserRoles(userId, request))
                .build();
    }
}
