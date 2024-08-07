package com.denmit.user_manager.controller;

import com.denmit.user_manager.dto.user.UserEditDto;
import com.denmit.user_manager.dto.user.UserLoginDto;
import com.denmit.user_manager.dto.user.UserRegisterDto;
import com.denmit.user_manager.dto.user.UserViewDto;
import com.denmit.user_manager.model.User;
import com.denmit.user_manager.service.UserService;
import com.denmit.user_manager.service.ValidationService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Api("User controller")
public class UserController {

    private final UserService userService;
    private final ValidationService validationService;

    @PostMapping("/reg")
    @ApiOperation(value = "Register a new user", authorizations = @Authorization(value = "Bearer"))
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegisterDto userRegisterDto,
                                          BindingResult bindingResult) {
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }

        User savedUser = userService.save(userRegisterDto);

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedUserLocation = currentUri + "/" + savedUser.getId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, savedUserLocation)
                .body(savedUser);
    }

    @PostMapping("/auth")
    @ApiOperation(value = "Authenticate and generate JWT token", authorizations = @Authorization(value = "Bearer"))
    public ResponseEntity<?> authenticationUser(@RequestBody @Valid UserLoginDto userLoginDto,
                                                BindingResult bindingResult) {
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }

        Map<Object, Object> response = userService.authenticateUser(userLoginDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users")
    @ApiOperation(value = "Get all users sorted by ID")
    public ResponseEntity<List<UserViewDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("users/{id}")
    @ApiOperation(value = "Get user by ID", authorizations = @Authorization(value = "Bearer"))
    public ResponseEntity<UserViewDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }

    @PutMapping("users/{id}")
    @ApiOperation(value = "Update user by ID", authorizations = @Authorization(value = "Bearer"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @Valid @RequestBody UserEditDto userEditDto) {
        return ResponseEntity.ok(userService.update(id, userEditDto));
    }

    @DeleteMapping("users/{id}")
    @ApiOperation(value = "Delete user by ID", authorizations = @Authorization(value = "Bearer"))
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    private boolean checkErrors(List<String> errorMessage) {
        return !errorMessage.isEmpty();
    }
}
