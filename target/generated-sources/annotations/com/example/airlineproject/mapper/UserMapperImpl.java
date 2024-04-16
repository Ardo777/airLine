package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.UserResponseDto;
import com.example.airlineproject.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-16T08:27:35+0400",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDto mapToDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto.UserResponseDtoBuilder userResponseDto = UserResponseDto.builder();

        userResponseDto.role( user.getRole() );
        userResponseDto.id( user.getId() );
        userResponseDto.name( user.getName() );
        userResponseDto.surname( user.getSurname() );
        userResponseDto.email( user.getEmail() );
        userResponseDto.picName( user.getPicName() );

        return userResponseDto.build();
    }
}
