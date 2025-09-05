package com.sample.busbooking.service.impl;

import com.sample.busbooking.dto.LoginRequestDTO;
import com.sample.busbooking.dto.UserRegistrationDTO;
import com.sample.busbooking.exception.DuplicateException;
import com.sample.busbooking.model.AppUser;
import com.sample.busbooking.repository.AppUserRepository;
import com.sample.busbooking.security.JwtUtil;
import com.sample.busbooking.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    static final Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);
    private final AppUserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Override
    public AppUser register(UserRegistrationDTO requestDto) {
        logger.info("Registering user with username: {}", requestDto.getUsername());
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            logger.warn("Email already registered: {}", requestDto.getEmail());
            throw new DuplicateException("Email is already registered");
        }
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            logger.warn("Username already taken: {}", requestDto.getUsername());
            throw new DuplicateException("Username is already taken");
        }
        if (userRepository.existsByMobileNumber(requestDto.getMobileNumber())) {
            logger.warn("Mobile number already registered: {}", requestDto.getMobileNumber());
            throw new DuplicateException("Mobile number is already registered");
        }
        AppUser user = convertToUser(requestDto);
        AppUser savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", savedUser.getUserId());
        return savedUser;
    }

    @Override
    public Map login(LoginRequestDTO requestDto) {
        logger.info("Login attempt for username: {}", requestDto.getUsername());
        AppUser user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> {
                    logger.warn("User not found:: {}", requestDto.getUsername());
                    return new UsernameNotFoundException("User not found ");
                });

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPasswordHash())) {
            logger.warn("Invalid credentials for username: {}", requestDto.getUsername());
            throw new BadCredentialsException("Invalid credentials");
        }
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        logger.info("Login successful for username: {}", user.getUsername());
        return Map.of("token", token);
    }

    public AppUser convertToUser(UserRegistrationDTO requestDto) {
        logger.info("Converting RegisterRequestDto to User entity for username: {}", requestDto.getUsername());
        AppUser user = modelMapper.map(requestDto, AppUser.class);
        user.setPasswordHash(passwordEncoder.encode(requestDto.getPassword()));
        return user;
    }
}
