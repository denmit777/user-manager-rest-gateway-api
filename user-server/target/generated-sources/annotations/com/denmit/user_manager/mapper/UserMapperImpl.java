package com.denmit.user_manager.mapper;

import com.denmit.user_manager.dto.user.UserRegisterDto;
import com.denmit.user_manager.dto.user.UserViewDto;
import com.denmit.user_manager.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-07T19:59:02+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private UserMapperUtil userMapperUtil;

    @Override
    public User toUser(UserRegisterDto userRegisterDto) {
        if ( userRegisterDto == null ) {
            return null;
        }

        User user = new User();

        user.setPassword( userMapperUtil.getEncodedPassword( userRegisterDto.getPassword() ) );
        user.setName( userRegisterDto.getName() );
        user.setEmail( userRegisterDto.getEmail() );

        return user;
    }

    @Override
    public UserViewDto toUserViewDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserViewDto userViewDto = new UserViewDto();

        userViewDto.setId( user.getId() );
        userViewDto.setName( user.getName() );
        userViewDto.setEmail( user.getEmail() );
        userViewDto.setRegisterDate( user.getRegisterDate() );

        return userViewDto;
    }

    @Override
    public List<UserViewDto> toUserViewDtoList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserViewDto> list = new ArrayList<UserViewDto>( users.size() );
        for ( User user : users ) {
            list.add( toUserViewDto( user ) );
        }

        return list;
    }
}
