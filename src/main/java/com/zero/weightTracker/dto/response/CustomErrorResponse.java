package com.zero.weightTracker.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CustomErrorResponse {
    private String code;
    private String message;
    private List<String> errors;
}
