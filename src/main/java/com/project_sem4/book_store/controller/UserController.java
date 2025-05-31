package com.project_sem4.book_store.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project_sem4.book_store.dto.request.user_request.ChangePasswordRequest;
import com.project_sem4.book_store.dto.request.user_request.UpdateUserRoleRequest;
import com.project_sem4.book_store.dto.request.user_request.UserUpdateRequest;
import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_role.RoleResponse;
import com.project_sem4.book_store.dto.response.data_response_user.UserResponse;
import com.project_sem4.book_store.enum_type.UserSearchType;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.service.FileStorageService;
import com.project_sem4.book_store.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    @Autowired
    UserService userService;
    FileStorageService fileStorageService;
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
    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable UUID userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .build();
    }

    @GetMapping("/{userId}/roles")
    public ApiResponse<Set<RoleResponse>> getRolesByUser(@PathVariable UUID userId) {
        return ApiResponse.<Set<RoleResponse>>builder()
                .result(userService.getRolesByUser(userId))
                .build();
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserResponse> updateUser(
            @PathVariable UUID userId,
            @RequestPart("data") String jsonData,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UserUpdateRequest request = objectMapper.readValue(jsonData, UserUpdateRequest.class);

            return ApiResponse.<UserResponse>builder()
                    .result(userService.updateUser(userId, request, avatarFile))
                    .build();
        }catch (AppException e) {
            throw e; // Giữ nguyên AppException, đừng ghi đè
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.INVALID_DATA_FORMAT); // nên có mã lỗi riêng cho deserialize
        }  catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }


    @GetMapping("/me")
    public ApiResponse<UserResponse> getProfileUser() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getProfileUser())
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
