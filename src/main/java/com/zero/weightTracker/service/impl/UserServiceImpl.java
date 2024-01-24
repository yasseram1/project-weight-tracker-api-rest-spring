package com.zero.weightTracker.service.impl;

import com.zero.weightTracker.dto.request.AuthRequest;
import com.zero.weightTracker.dto.response.AuthResponse;
import com.zero.weightTracker.dto.response.DefaultResponse;
import com.zero.weightTracker.entity.User;
import com.zero.weightTracker.repository.UserRepository;
import com.zero.weightTracker.service.JwtService;
import com.zero.weightTracker.service.MailService;
import com.zero.weightTracker.service.UserService;
import com.zero.weightTracker.util.Codes;
import com.zero.weightTracker.util.CustomResponses;
import com.zero.weightTracker.util.ErrorsUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ErrorsUtil errorsUtil;

    @Autowired
    private CustomResponses customResponses;

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public ResponseEntity<?> createUser(@Valid User user, BindingResult bindingResult) {
        List<String> fieldsErrors = new ArrayList<>();

        if(userRepository.existsByUsername(user.getUsername())) {
            fieldsErrors.add("El nombre de usuario ya está registrado");
            return customResponses.customErrorResponse(fieldsErrors,"Error al registrar el usuario");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            fieldsErrors.add("El correo electrónico ya está registrado");
            return customResponses.customErrorResponse(fieldsErrors,"Error al registrar el usuario");
        }

        if(bindingResult.hasErrors()) {
            fieldsErrors.addAll(errorsUtil.getListErrors(bindingResult));
            return customResponses.customErrorResponse(fieldsErrors,"Error al registrar el usuario");
        }

        try {
            userRepository.save(user);
            mailService.sendVerificationEmail(user.getEmail(), user.getConfirmationToken(), user.getUsername());
            return customResponses.customRegisterResponse(user, "Usuario creado con éxito.");
        } catch (Exception exception) {
            fieldsErrors.add(exception.getMessage());
            return customResponses.customErrorResponse(fieldsErrors, "Error al registrar el usuario");
        }
    }

    @Override
    public ResponseEntity<?> login(AuthRequest authRequest) {

        if(!userRepository.existsByUsername(authRequest.getUsername())) {
            return customResponses.customErrorResponse(List.of("El usuario no existe"), "Error al iniciar sesión");
        }

        // Logica para verificar si el password del usuario coincide con el password dado el el authRequest, se esta usando passwordEncoder de Bcryt
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
            authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            return customResponses.customErrorResponse(List.of("Las credenciales proporcionadas no son válidas"), "Error de autenticación");
        }

        User user = userRepository.findByUsername(authRequest.getUsername()).get();

        if(user.getStatus().equals("false")) {
            return customResponses.customErrorResponse(List.of("Debe verificar el email del usuario para poder iniciar sesión"), "Email no verificado");
        }

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));

        return new ResponseEntity<>(new AuthResponse(jwt), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DefaultResponse> verificationEmailWithToken(String token, String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getConfirmationToken().equals(token)) {
                user.setConfirmationToken(null);
                user.setStatus("true");
                userRepository.save(user);
                return customResponses.defaultResponse(Codes.SUCCESS.name(), "El email se ha verificado exitosamente.", HttpStatus.OK);
            } else {
                return customResponses.defaultResponse(Codes.ERROR.name(), "El token proporcionado no es válido.", HttpStatus.BAD_REQUEST);
            }
        }
        return customResponses.defaultResponse(Codes.ERROR.name(), "Usuario no encontrado para el nombre de usuario proporcionado.", HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        extraClaims.put("permissions", user.getAuthorities());

        return extraClaims;
    }

}
