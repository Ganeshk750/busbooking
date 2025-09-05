package com.sample.busbooking.service;

import com.sample.busbooking.dto.LoginRequestDTO;
import com.sample.busbooking.dto.UserRegistrationDTO;
import com.sample.busbooking.model.AppUser;

import java.util.Map;

public interface AppUserService {
    AppUser register(UserRegistrationDTO requestDto);
    Map login(LoginRequestDTO requestDto);
}
