package com.denmit.user_manager.mapper;

import com.denmit.user_manager.dto.user.UserEditDto;
import com.denmit.user_manager.model.User;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Named("UserMapperUtil")
@Component
@RequiredArgsConstructor
public class UserMapperUtil {

    private final PasswordEncoder passwordEncoder;

    @Named("getEncodedPassword")
    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void editUser(User user, UserEditDto userDto) {
        user.setName(userDto.getName());
        user.setPassword(getEncodedPassword(userDto.getPassword()));
    }
}

