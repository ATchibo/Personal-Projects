package com.example.assignment1_1.mappers;

import com.example.assignment1_1.domain.AppUser;
import com.example.assignment1_1.dtos.SignUpDto;
import com.example.assignment1_1.dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(AppUser user);

    @Mapping(target = "password", ignore = true)
    AppUser signUpToUser(SignUpDto userDto);

    @Mapping(target = "password", ignore = true)
    SignUpDto userToSignUp(AppUser user);
}
