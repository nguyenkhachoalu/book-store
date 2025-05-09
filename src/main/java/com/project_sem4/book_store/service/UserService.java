package com.project_sem4.book_store.service;

import com.project_sem4.book_store.dto.mapper.UserMapper;
import com.project_sem4.book_store.dto.request.user_request.ChangePasswordRequest;
import com.project_sem4.book_store.dto.request.user_request.UpdateUserRoleRequest;
import com.project_sem4.book_store.dto.request.user_request.UserUpdateRequest;
import com.project_sem4.book_store.dto.response.PagedResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public PagedResponse<UserResponse> getPagedUsers(String keyword, UserSearchType type, Pageable pageable) {
        try{
            Page<User> userPage = userRepository.searchUsers(keyword, type, pageable);
            List<UserResponse> userResponses = userPage.getContent()
                    .stream()
                    .map(userMapper::toUserResponse)
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
    public UserResponse updateUser(UUID userId, UserUpdateRequest request){
        try{
            User user = userRepository.findById(userId)
                    .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_EXISTED));
            if (!ValidateInput.isValidEmail(request.getEmail())) {
                throw new AppException(ErrorCode.INVALID_EMAIL_FORMAT);
            }
            if (!ValidateInput.isValidPhoneNumber(request.getPhone())) {
                throw new AppException(ErrorCode.INVALID_PHONE_FORMAT);
            }
            user.setFullName(request.getFull_name());
            user.setPhone(request.getPhone());
            user.setEmail(request.getEmail());
            user.setUpdatedAt(LocalDateTime.now());
            user.setAvatar(request.getAvata_path());
            userRepository.save(user);
            return userMapper.toUserResponse(user);
        } catch (AppException e) {
            throw e;
        }catch (Exception e) {
            log.error("Update user failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
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

}
