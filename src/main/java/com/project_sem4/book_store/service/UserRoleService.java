package com.project_sem4.book_store.service;

import com.project_sem4.book_store.entity.Role;
import com.project_sem4.book_store.entity.User;
import com.project_sem4.book_store.entity.UserRole;
import com.project_sem4.book_store.entity.UserRoleId;
import com.project_sem4.book_store.repository.RoleRepository;
import com.project_sem4.book_store.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRoleService {

    UserRoleRepository userRoleRepository;
    RoleRepository roleRepository;

    @Transactional
    public void assignRoles(User user, List<String> roleCodes) {
        for (String roleCode : roleCodes) {
            Optional<Role> roleOpt = roleRepository.findByRoleCode(roleCode);
            if (roleOpt.isPresent()) {
                Role role = roleOpt.get();
                UserRole userRole = UserRole.builder()
                        .userId(user.getId())
                        .roleId(role.getId())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                userRoleRepository.save(userRole);
            }
        }
    }
}