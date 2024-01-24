package com.zero.weightTracker.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DefaultResponse {
    private String code;
    private String message;
}
