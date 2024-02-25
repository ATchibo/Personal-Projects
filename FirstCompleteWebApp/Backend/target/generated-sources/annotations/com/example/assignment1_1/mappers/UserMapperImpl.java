package com.example.assignment1_1.mappers;

import com.example.assignment1_1.domain.AppUser;
import com.example.assignment1_1.dtos.SignUpDto;
import com.example.assignment1_1.dtos.SignUpDto.SignUpDtoBuilder;
import com.example.assignment1_1.dtos.UserDto;
import com.example.assignment1_1.dtos.UserDto.UserDtoBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-05T13:04:14+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(AppUser user) {
        if ( user == null ) {
            return null;
        }

        UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.firstName( user.getFirstName() );
        userDto.lastName( user.getLastName() );
        userDto.email( user.getEmail() );
        userDto.username( user.getUsername() );
        userDto.role( user.getRole() );
        userDto.location( user.getLocation() );

        return userDto.build();
    }

    @Override
    public AppUser signUpToUser(SignUpDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        AppUser appUser = new AppUser();

        appUser.setFirstName( userDto.getFirstName() );
        appUser.setLastName( userDto.getLastName() );
        appUser.setEmail( userDto.getEmail() );
        appUser.setUsername( userDto.getUsername() );
        appUser.setRole( userDto.getRole() );
        appUser.setLocation( userDto.getLocation() );

        return appUser;
    }

    @Override
    public SignUpDto userToSignUp(AppUser user) {
        if ( user == null ) {
            return null;
        }

        SignUpDtoBuilder signUpDto = SignUpDto.builder();

        signUpDto.firstName( user.getFirstName() );
        signUpDto.lastName( user.getLastName() );
        signUpDto.email( user.getEmail() );
        signUpDto.username( user.getUsername() );
        signUpDto.role( user.getRole() );
        signUpDto.location( user.getLocation() );

        return signUpDto.build();
    }
}
