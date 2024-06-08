package com.example.airlineproject.util;

import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUtil fileUtil;

    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Checking for admin user...");
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            log.info("Admin user not found, creating new admin user...");
            User adminUser = User.builder()
                    .name("Admin")
                    .surname("User")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123123"))
                    .role(UserRole.ADMIN)
                    .isActive(true)
                    .verificationCode(fileUtil.createVerificationCode())
                    .build();
            userRepository.save(adminUser);
            log.info("Admin user created with email: admin@gmail.com");
        } else {
            log.info("Admin user already exists.");
        }
    }
}
