package com.project_sem4.book_store.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;


@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationIniConfig {

    @Bean
    ApplicationRunner applicationRunner(InitialDataService initialDataService) {
        return args -> {
            LocalDateTime now = LocalDateTime.now();
            initialDataService.initializeRoles(now);
            initialDataService.initializeAdmin(now, "admin", "admin");
        };
    }

}

