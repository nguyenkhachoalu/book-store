package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.mapper.UserMapper;
import com.project_sem4.book_store.dto.request.user_request.ChangePasswordRequest;
import com.project_sem4.book_store.dto.request.user_request.UpdateUserRoleRequest;
import com.project_sem4.book_store.dto.request.user_request.UserUpdateRequest;
import com.project_sem4.book_store.dto.response.PagedResponse;
import com.project_sem4.book_store.dto.response.data_response_role.RoleResponse;
import com.project_sem4.book_store.dto.response.data_response_user.UserResponse;
import com.project_sem4.book_store.entity.Role;
import com.project_sem4.book_store.entity.User;
import com.project_sem4.book_store.enum_type.UserSearchType;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.RoleRepository;
import com.project_sem4.book_store.repository.UserRepository;
import com.project_sem4.book_store.validation.ValidateInput;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    RoleRepository roleRepository;
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    FileStorageService fileStorageService;
    @NonFinal
    @Value("${app.base-url}")
    String baseUrl;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public PagedResponse<UserResponse> getPagedUsers(String keyword, UserSearchType type, Pageable pageable) {
        try{
            Page<User> userPage = userRepository.searchUsers(keyword, type, pageable);
            List<UserResponse> userResponses = userPage.getContent()
                    .stream()
                    .map(user -> {
                        UserResponse response = userMapper.toUserResponse(user);
                        if (user.getAvatar() != null) {
                            response.setAvatar(baseUrl + "/uploads/avatars/" + user.getAvatar());
                        }
                        return response;
                    })
                    .toList();


            return PagedResponse.<UserResponse>builder()
                    .content(userResponses)
                    .pageNumber(userPage.getNumber())
                    .pageSize(userPage.getSize())
                    .totalElements(userPage.getTotalElements())
                    .totalPages(userPage.getTotalPages())
                    .hasNext(userPage.hasNext())
                    .hasPrevious(userPage.hasPrevious())
                    .build();
        }catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Get paged users false ",e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    public UserResponse updateUser(UUID userId, UserUpdateRequest request, MultipartFile avatarFile) {
        try{
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            if (!user.getEmail().equals(request.getEmail()) &&
                    userRepository.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_EXISTS);
            }

            if (!user.getPhone().equals(request.getPhone()) &&
                    userRepository.existsByPhone(request.getPhone())) {
                throw new AppException(ErrorCode.PHONE_EXISTS);
            }
            if (!ValidateInput.isValidEmail(request.getEmail())) {
                throw new AppException(ErrorCode.INVALID_EMAIL_FORMAT);
            }
            if (!ValidateInput.isValidPhoneNumber(request.getPhone())) {
                throw new AppException(ErrorCode.INVALID_PHONE_FORMAT);
            }

            String newAvatar = handleAvatarUpload(avatarFile, user.getAvatar());

            user.setFullName(request.getFull_name());
            user.setPhone(request.getPhone());
            user.setEmail(request.getEmail());
            user.setUpdatedAt(LocalDateTime.now());
            user.setAvatar(newAvatar);

            userRepository.save(user);
            return userMapper.toUserResponse(user);
        }catch (AppException e) {
            throw e;
        }catch (Exception e) {
            log.error("Update user failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    public UserResponse getProfileUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserResponse response = userMapper.toUserResponse(user);

        if (user.getAvatar() != null) {
            response.setAvatar(baseUrl + "/uploads/avatars/" + user.getAvatar());
        }

        return response;
    }

    public UserResponse changePassword(UUID userId, ChangePasswordRequest request){
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            // Kiểm tra mật khẩu cũ có đúng không
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new AppException(ErrorCode.INVALID_PASSWORD); // mã lỗi tự định nghĩa
            }

            // Kiểm tra mật khẩu mới trùng với mật khẩu cũ
            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new AppException(ErrorCode.PASSWORD_SAME_AS_OLD); // mã lỗi khác nếu muốn
            }

            // Cập nhật mật khẩu mới (sau khi mã hóa)
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            return userMapper.toUserResponse(user);
        } catch (AppException e) {
            throw e;
        }catch (Exception e) {
            log.error("Change password failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

    }

    @Transactional
    public String updateUserRoles(UUID userId, UpdateUserRoleRequest request) {
        if (request.getRoleCodes() == null || request.getRoleCodes().isEmpty()) {
            throw new AppException(ErrorCode.ROLE_LIST_EMPTY);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Role> requestedRoles = roleRepository.findByRoleCodeIn(new ArrayList<>(request.getRoleCodes()));
        if (requestedRoles.isEmpty()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        user.setRoles(new HashSet<>(requestedRoles)); // Gán trực tiếp
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user); // JPA tự xử lý bảng trung gian

        return "Cập nhật quyền thành công";
    }


    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserResponse response = userMapper.toUserResponse(user);

        if (user.getAvatar() != null) {
            response.setAvatar(baseUrl + "/uploads/avatars/" + user.getAvatar());
        }

        return response;
    }

    public Set<RoleResponse> getRolesByUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return user.getRoles().stream()
                .map(role -> RoleResponse.builder()
                        .id(role.getId())
                        .roleName(role.getRoleName())
                        .roleCode(role.getRoleCode())
                        .build())
                .collect(Collectors.toSet());
    }





    public String handleAvatarUpload(MultipartFile avatarFile, String oldFilename) {
        if (avatarFile == null || avatarFile.isEmpty()) return oldFilename;

        // Xóa ảnh cũ nếu có
        if (oldFilename != null) {
            try {
                Files.deleteIfExists(Paths.get("uploads/avatars", oldFilename));
            } catch (IOException e) {
                log.warn("Không thể xóa ảnh cũ: {}", oldFilename);
            }
        }

        return fileStorageService.storeFile(avatarFile, "avatars");
    }
}
