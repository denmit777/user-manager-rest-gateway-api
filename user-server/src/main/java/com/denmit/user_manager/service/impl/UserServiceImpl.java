package com.denmit.user_manager.service.impl;

import com.denmit.user_manager.config.security.jwt.JwtTokenProvider;
import com.denmit.user_manager.dto.user.UserEditDto;
import com.denmit.user_manager.mapper.UserMapper;
import com.denmit.user_manager.dto.user.UserLoginDto;
import com.denmit.user_manager.dto.user.UserRegisterDto;
import com.denmit.user_manager.dto.user.UserViewDto;
import com.denmit.user_manager.exception.UserIsPresentException;
import com.denmit.user_manager.exception.UserNotFoundException;
import com.denmit.user_manager.mapper.UserMapperUtil;
import com.denmit.user_manager.model.CustomUserDetails;
import com.denmit.user_manager.model.User;
import com.denmit.user_manager.repository.UserRepository;
import com.denmit.user_manager.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class.getName());

    private static final String USER_IS_PRESENT = "User with login %s is already present";
    private static final String USER_NOT_FOUND = "User with login %s not found";
    private static final String USER_HAS_ANOTHER_PASSWORD = "User with login %s has another password. " +
            "Go to register or enter valid credentials";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final UserMapperUtil userMapperUtil;

    @Override
    @Transactional
    public User save(UserRegisterDto userDto) {
        checkUserBeforeSave(userDto);

        User user = userMapper.toUser(userDto);
        user.setRegisterDate(LocalDateTime.now());

        userRepository.save(user);

        LOGGER.info("New user : {}", user);

        return user;
    }

    @Override
    @Transactional
    public Map<Object, Object> authenticateUser(UserLoginDto userDto) {
        User user = getByLoginAndPassword(userDto.getLogin(), userDto.getPassword());

        String token = jwtTokenProvider.createToken(user.getEmail());

        Map<Object, Object> response = new HashMap<>();

        response.put("userName", user.getEmail());
        response.put("token", token);

        return response;
    }

    @Override
    @Transactional
    public List<UserViewDto> getAll() {
        List<User> users = userRepository.findAll(PageRequest.of(0, 25,
                Sort.by(Sort.Direction.ASC, "id")));

        LOGGER.info("Users: {}", users);

        return userMapper.toUserViewDtoList(users);
    }

    @Override
    @Transactional
    public UserViewDto getUserInfo(Long id) {
        return userMapper.toUserViewDto(getById(id));
    }

    @Override
    @Transactional
    public User update(Long id, UserEditDto userDto) {
        User updatedUser = getById(id);

        userMapperUtil.editUser(updatedUser, userDto);

        userRepository.save(updatedUser);

        LOGGER.info("Updated user : {}", updatedUser);

        return updatedUser;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = getById(id);

        userRepository.delete(user);

        LOGGER.info("User was successfully deleted");
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        return new CustomUserDetails(getByLogin(login));
    }

    public User getByLoginAndPassword(String login, String password) {
        User user = getByLogin(login);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            LOGGER.info("User : {}", user);

            return user;
        }
        LOGGER.error(String.format(USER_HAS_ANOTHER_PASSWORD, login));

        throw new UserNotFoundException(String.format(USER_HAS_ANOTHER_PASSWORD, login));
    }

    private User getByLogin(String login) {
        return userRepository.findByEmail(login)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, login)));
    }

    private User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    private boolean isUserPresent(UserRegisterDto userDto) {
        List<User> users = (List<User>) userRepository.findAll();

        return users.stream().anyMatch(user -> user.getEmail().equals(userDto.getEmail()));
    }

    private void checkUserBeforeSave(UserRegisterDto userDto) {
        if (isUserPresent(userDto)) {
            LOGGER.error(String.format(USER_IS_PRESENT, userDto.getEmail()));

            throw new UserIsPresentException(String.format(USER_IS_PRESENT, userDto.getEmail()));
        }
    }
}
