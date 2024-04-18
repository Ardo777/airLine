package com.example.airlineproject.service;

import com.example.airlineproject.dto.ChangePasswordDto;
import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.security.SpringUser;
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

    List<UserResponseDto> getAllByFilter(String keyword);

    void update(User user, SpringUser springUser, MultipartFile multipartFile) throws IOException;

    void changePassword(ChangePasswordDto changePasswordDto, SpringUser springUser);

    void updateEmail(SpringUser springUser, String email);
    Long getUsersCount();
}
