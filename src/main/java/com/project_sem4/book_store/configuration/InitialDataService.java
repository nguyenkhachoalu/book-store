package com.project_sem4.book_store.configuration;

import com.project_sem4.book_store.entity.Role;
import com.project_sem4.book_store.entity.User;
import com.project_sem4.book_store.entity.UserRole;
import com.project_sem4.book_store.repository.RoleRepository;
import com.project_sem4.book_store.repository.UserRepository;
import com.project_sem4.book_store.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InitialDataService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final List<String> DEFAULT_ROLES = List.of("ADMIN", "USER", "MANAGER", "SHIPPER", "SELLER");

    @Transactional
    public void initializeRoles(LocalDateTime now) {
        for (String roleCode : DEFAULT_ROLES) {
            if(!roleRepository.existsByRoleCode(roleCode)){
                roleRepository.save(Role.builder()
                        .roleCode(roleCode)
                        .roleName(roleCode + " Role")
                        .createdAt(now)
                        .updatedAt(now)
                        .isActive(true)
                        .build());
            }
        }
    }


    @Transactional
    public void initializeAdmin(LocalDateTime now, String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) return;

        Role adminRole = roleRepository.findByRoleCode("ADMIN")
                .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

        User admin = User.builder()
                .username(username)
                .fullName("System Administrator")
                .password(passwordEncoder.encode(password))
                .email("admin@example.com")
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        userRepository.save(admin);

        userRoleRepository.save(UserRole.builder()
                .userId(admin.getId())
                .roleId(adminRole.getId())
                .createdAt(now)
                .updatedAt(now)
                .build());
    }
}
