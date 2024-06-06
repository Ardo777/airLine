package com.example.airlineproject.service;

import com.example.airlineproject.dto.ChangePasswordDto;
import com.example.airlineproject.dto.UserRegisterDto;
import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.security.SpringUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(UserRegisterDto userRegisterDto, MultipartFile multipartFile) throws IOException;

    UserResponseDto findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(int id);

    void deleteById(int id);

    boolean recoveryPassword(User user, String newPassword);

    List<UserResponseDto> getAllByFilter(String keyword);

    boolean update(User user, SpringUser springUser, MultipartFile multipartFile) throws IOException;

    boolean changePassword(ChangePasswordDto changePasswordDto, SpringUser springUser);

    void updateEmail(SpringUser springUser, String email);

    void processEmailUpdate(SpringUser springUser, String email, String verificationCode);

    Long getUsersCount();

    User findRandomAdmin();

    List<User> findAllByCompany(Company company);

    void subscribeToCompany(int companyId, User user);

    boolean delete(SpringUser springUser, String password);

    boolean restoreUser(String email, String verificationCode);

    void verify(String email);
}
