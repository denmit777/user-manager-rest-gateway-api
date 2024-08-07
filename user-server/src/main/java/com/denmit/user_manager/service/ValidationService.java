package com.denmit.user_manager.service;

import org.springframework.validation.BindingResult;

import java.util.List;

public interface ValidationService {

    List<String> generateErrorMessage(BindingResult bindingResult);
}
