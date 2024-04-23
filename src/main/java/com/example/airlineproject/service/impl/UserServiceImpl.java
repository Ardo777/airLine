package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.UserRegisterDto;
import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.entity.QUser;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.mapper.UserMapper;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.service.MailService;
import com.example.airlineproject.service.UserService;
import com.example.airlineproject.util.FileUtil;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final  MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final FileUtil fileUtil;
    private final EntityManager entityManager;


    @Override
    public User save(UserRegisterDto userRegisterDto, MultipartFile multipartFile) throws IOException {

        Optional<User> byEmail = userRepository.findByEmail(userRegisterDto.getEmail());
        if (byEmail.isPresent() && !byEmail.get().isActive()) {
            userRepository.deleteById(byEmail.get().getId());
            validation(userRegisterDto, multipartFile);
            User user1 = userMapper.mapToUser(userRegisterDto);
            User save = userRepository.save(user1);
            log.info("User saved successfully: {}", save.getEmail());
            return save;
        }
        validation(userRegisterDto, multipartFile);
        User user1 = userMapper.mapToUser(userRegisterDto);
        User save = userRepository.save(user1);
        log.info("User saved successfully: {}", save.getEmail());
        return user1;
    }

    private void validation(UserRegisterDto userRegisterDto, MultipartFile multipartFile) throws IOException {
        userRegisterDto.setUserRole(UserRole.USER);
        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        String picName = fileUtil.saveFile(multipartFile);
        userRegisterDto.setPicName(picName);
        userRegisterDto.setVerificationCode(fileUtil.createVerificationCode());
        mailService.sendMail(userRegisterDto);
    }


    @Override
    public UserResponseDto findByEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            return userMapper.mapToUserResponseDto(user);
        }
        return null;
    }

    @Override
    public void deleteById(int id) {

        Optional<User> byId = findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            userRepository.deleteById(id);
            fileUtil.deletePicture(user.getPicName());
            log.info("User with this id deleted successfully: {}", id);
        } else {
            log.warn("User with id {} not found.", id);
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


    @Override
    public boolean recoveryPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    public List<UserResponseDto> getAllByFilter(String keyword) {
        String name = null;
        String surname = null;
        if (keyword.contains(" ")) {
            String[] keywords = keyword.split("\\s+");
            name = keywords[0];
            surname = keywords[1];
        }

        log.debug("Filtering users by keyword: {}", keyword);

        JPAQuery<User> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        JPAQueryBase<User, JPAQuery<User>> from = query.from(qUser);
        List<User> fetch;
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(surname)) {
            from.where(qUser.name.startsWith(name).and(qUser.surname.startsWith(surname)));
            fetch = query.fetch();
        } else {
            from.where(qUser.name.startsWith(keyword).or(qUser.surname.startsWith((keyword))));
            fetch = query.fetch();
        }
        fetch = fetch.stream()
                .filter(user -> !UserRole.ADMIN.equals(user.getRole()))
                .collect(Collectors.toList());

        List<UserResponseDto> userFilterDtoList = new ArrayList<>();
        for (User user : fetch) {
            userFilterDtoList.add(userMapper.mapToDto(user));
        }

        log.debug("Filtered users count: {}", userFilterDtoList.size());

        return userFilterDtoList;
    }

    @Override
    public Long getUsersCount() {
        return userRepository.count();
    }

}
