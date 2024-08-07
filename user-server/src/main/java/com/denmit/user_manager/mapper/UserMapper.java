package com.denmit.user_manager.mapper;

import com.denmit.user_manager.dto.user.UserRegisterDto;
import com.denmit.user_manager.dto.user.UserViewDto;
import com.denmit.user_manager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapperUtil.class})
public interface UserMapper {

    @Mapping(target = "password", qualifiedByName = {"UserMapperUtil", "getEncodedPassword"})
    User toUser(UserRegisterDto userRegisterDto);

    UserViewDto toUserViewDto(User user);

    List<UserViewDto> toUserViewDtoList(List<User> users);
}
