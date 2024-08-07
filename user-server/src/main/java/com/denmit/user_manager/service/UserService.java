package com.denmit.user_manager.service;

import com.denmit.user_manager.dto.user.UserEditDto;
import com.denmit.user_manager.dto.user.UserLoginDto;
import com.denmit.user_manager.dto.user.UserRegisterDto;
import com.denmit.user_manager.dto.user.UserViewDto;
import com.denmit.user_manager.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User save(UserRegisterDto userDto);

    Map<Object, Object> authenticateUser(UserLoginDto userDto);

    List<UserViewDto> getAll();

    UserViewDto getUserInfo(Long id);

    User update(Long id, UserEditDto userDto);

    void deleteById(Long id);
}
