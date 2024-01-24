package com.zero.weightTracker.controller;

import com.zero.weightTracker.dto.request.AuthRequest;
import com.zero.weightTracker.dto.request.UserRegisterRequest;
import com.zero.weightTracker.dto.response.DefaultResponse;
import com.zero.weightTracker.entity.User;
import com.zero.weightTracker.service.JwtService;
import com.zero.weightTracker.service.UserService;
import com.zero.weightTracker.util.UserUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest, BindingResult bindingResult) {
        User user = userUtil.userDtoToUser(userRegisterRequest);
        return userService.createUser(user, bindingResult);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        return userService.login(authRequest);
    }

    @GetMapping("/verification")
    public ResponseEntity<DefaultResponse> verificationEmail(@RequestParam String token, @RequestParam String username) {
            return userService.verificationEmailWithToken(token, username);
    }

}
