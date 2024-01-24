package com.zero.weightTracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class NewGoalRequest {

    @NotNull(message = "Ingrese un peso inicial valido (Kg)")
    private Double startWeight;

    @NotNull(message = "Ingrese una meta de peso valida (Kg)")
    private Double goalWeight;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

}
