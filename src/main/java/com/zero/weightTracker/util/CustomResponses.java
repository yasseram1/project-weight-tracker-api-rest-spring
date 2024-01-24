package com.zero.weightTracker.util;

import com.zero.weightTracker.dto.response.CustomErrorResponse;
import com.zero.weightTracker.dto.response.CustomRegisterResponse;
import com.zero.weightTracker.dto.response.DefaultResponse;
import com.zero.weightTracker.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CustomResponses {

    public ResponseEntity<CustomRegisterResponse> customRegisterResponse(User user, String message) {
        return new ResponseEntity<>(
                CustomRegisterResponse
                        .builder()
                        .code(Codes.SUCCESS.name())
                        .message(message)
                        .registrationDateTime(LocalDateTime.now())
                        .verificationLink(user.getConfirmationToken())
                        .build(),
                HttpStatus.CREATED);
    }

    public ResponseEntity<?> customErrorResponse(List<String> fieldsErrors, String message) {
        return new ResponseEntity<>(
                CustomErrorResponse
                        .builder()
                        .code(Codes.ERROR.name())
                        .message(message)
                        .errors(fieldsErrors)
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<DefaultResponse> defaultResponse(String code, String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(DefaultResponse
                .builder()
                .code(code)
                .message(message)
                .build()
                ,httpStatus);
    }

}
