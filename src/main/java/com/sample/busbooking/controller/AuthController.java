package com.sample.busbooking.controller;

import com.sample.busbooking.dto.LoginRequestDTO;
import com.sample.busbooking.dto.UserRegistrationDTO;
import com.sample.busbooking.model.AppUser;
import com.sample.busbooking.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AuthController {

    static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AppUserService userService;

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody @Valid UserRegistrationDTO requestDto) {
        AppUser user = userService.register(requestDto);
        logger.info("Received request for new user: {}", requestDto.getUsername());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map> login(@Valid @RequestBody LoginRequestDTO dto) {
        Map token = userService.login(dto);
        logger.info("Received login request for new user: {}", dto.getUsername());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
