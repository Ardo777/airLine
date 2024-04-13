package com.example.airlineproject.service;

import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user, MultipartFile multipartFile) throws IOException;

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(int id);
    void deleteById(int id);

    void verificationCodeSending(User user, String email);

    boolean recoveryPassword(User user, String newPassword);

    Page<User> findAll(Pageable pageable);

    List<UserResponseDto> getAllByFilter(String keyword);

}
