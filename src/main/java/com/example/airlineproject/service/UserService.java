package com.example.airlineproject.service;

import com.example.airlineproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    User save(User user, MultipartFile multipartFile) throws IOException;

    Optional<User> findByEmail(String email);

    void deleteById(int id);

    Optional<User> findById(int id);

    Page<User> findAll(Pageable pageable);
}
