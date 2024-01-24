package com.zero.weightTracker.service;

import com.zero.weightTracker.dto.request.AuthRequest;
import com.zero.weightTracker.dto.response.DefaultResponse;
import com.zero.weightTracker.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByUsername(String email);
    ResponseEntity<?> createUser(User user, BindingResult br);
    ResponseEntity<?> login(AuthRequest authRequest);
    ResponseEntity<DefaultResponse> verificationEmailWithToken(String token, String username);
}
