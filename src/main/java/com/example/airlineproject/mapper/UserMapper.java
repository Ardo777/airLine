package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role", target = "role")
    UserResponseDto mapToDto(User user);
}
