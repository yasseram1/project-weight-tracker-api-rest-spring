package com.zero.weightTracker.util;

import com.zero.weightTracker.dto.request.UserRegisterRequest;
import com.zero.weightTracker.entity.User;
import com.zero.weightTracker.repository.UserRepository;
import com.zero.weightTracker.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserUtil {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public User userDtoToUser(UserRegisterRequest userRegisterRequest) {
        return User.builder()
                .username(userRegisterRequest.getUsername())
                .email(userRegisterRequest.getEmail())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .confirmationToken(getConfirmationToken())
                .status("false")
                .role(Role.USER)
                .build();
    }

    private static String getConfirmationToken() {
        return UUID.randomUUID().toString();
    }

    public User getAuthenticatedUserFromToken(HttpServletRequest http) {
        String jwt = http.getHeader("Authorization").split(" ")[1];
        String username = jwtService.getUsernameFromToken(jwt);
        return userRepository.findByUsername(username).get();
    }

}
