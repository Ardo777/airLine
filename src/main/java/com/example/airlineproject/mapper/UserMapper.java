package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.UserRegisterDto;
import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role", target = "role")
    UserResponseDto mapToDto(User user);

    @Mapping(source = "userRole", target = "role")
    User mapToUser(UserRegisterDto userRegisterDto);

    UserRegisterDto mapToUserRegisterDto(User user);

    UserResponseDto mapToUserResponseDto(User user);
}
