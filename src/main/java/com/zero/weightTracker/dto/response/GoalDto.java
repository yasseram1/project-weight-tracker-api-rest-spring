package com.zero.weightTracker.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class GoalDto {
    private Double startWeight;
    private Double goalWeight;
    private LocalDateTime startDate;
}
