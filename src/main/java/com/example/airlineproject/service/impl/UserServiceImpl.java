package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.UserRole;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final MailServiceImpl mailService;

    private final PasswordEncoder passwordEncoder;

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @Override
    public User save(User user, MultipartFile multipartFile) throws IOException {
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isEmpty()) {
            user.setRole(UserRole.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                File picturesDir = new File(uploadDirectory);
                if (!picturesDir.exists()) {
                    picturesDir.mkdirs();
                }
                String filePath = picturesDir.getAbsolutePath() + "/" + picName;
                File file = new File(filePath);
                multipartFile.transferTo(file);
                user.setPicName(picName);
            }
            String lUUID = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
            String uuid = lUUID.substring(0, Math.min(lUUID.length(), 6));
            mailService.sendMail(user);
            user.setVerificationCode(uuid);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteById(int id) {
        Optional<User> byId = findById(id);
        if (byId.isPresent()) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }
}
