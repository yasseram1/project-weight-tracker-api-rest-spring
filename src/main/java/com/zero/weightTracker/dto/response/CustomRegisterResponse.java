package com.zero.weightTracker.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CustomRegisterResponse {
    private String code;
    private String message;
    private LocalDateTime registrationDateTime;
    private String verificationLink;
}
