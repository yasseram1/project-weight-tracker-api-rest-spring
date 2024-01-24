package com.zero.weightTracker.dto.request;

import jakarta.persistence.Column;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewWeightRecordRequest {
    @NotNull(message = "Ingrese un peso (Kg)")
    private Double weight;

    @NotNull(message = "Ingrese la fecha de registro del peso")
    private LocalDateTime recordDate;
}
