package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.service.UserService;
import com.example.airlineproject.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final MailServiceImpl mailService;

    private final PasswordEncoder passwordEncoder;

    private final FileUtil fileUtil;

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @Override
    public User save(User user, MultipartFile multipartFile) throws IOException {
        log.info("Saving user: " + user.getEmail());

        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail.isPresent() && !byEmail.get().isActive()) {
            userRepository.deleteById(byEmail.get().getId());
            validation(user, multipartFile);
            User save = userRepository.save(user);
            log.info("User saved successfully: " + save.getEmail());
            return user;
        } else {
            validation(user, multipartFile);
            User save = userRepository.save(user);
            log.info("User saved successfully: " + save.getEmail());
            return user;
        }
    }

    private void validation(User user, MultipartFile multipartFile) throws IOException {
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        addPicture(multipartFile, user);
        String lUUID = String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16));
        String uuid = lUUID.substring(0, Math.min(lUUID.length(), 6));
        mailService.sendMail(user);
        user.setVerificationCode(uuid);
    }

    private void addPicture(MultipartFile multipartFile, User user) throws IOException {
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
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteById(int id) {
        log.info("Deleting user with id: " + id);

        Optional<User> byId = findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            userRepository.deleteById(id);
            fileUtil.deletePicture(user.getPicName());
            log.info("User deleted successfully: " + user.getEmail());
        }else {
            log.warn("User with id " + id + " not found.");
        }
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
