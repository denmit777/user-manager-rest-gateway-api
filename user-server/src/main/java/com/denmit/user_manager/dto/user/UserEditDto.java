package com.denmit.user_manager.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserEditDto {

    private static final String FIELDS_IS_EMPTY = "Please fill out the required field.";
    private static final String WRONG_NAME_SIZE = "Name must be between 3 and 30 symbols";
    private static final String INVALID_DATA = "Please make sure you are using a valid password";

    @NotBlank(message = FIELDS_IS_EMPTY)
    @Size(max = 30, min = 3, message = WRONG_NAME_SIZE)
    private String name;

    @NotBlank(message = FIELDS_IS_EMPTY)
    @Pattern(regexp = "((?=.*d)(?=.*[\\p{Lu}])(?=.*[\\d])(?=.*[\\p{Ll}])(?=.*[\\p{Punct}]).{6,20})",
            message = INVALID_DATA)
    private String password;
}
