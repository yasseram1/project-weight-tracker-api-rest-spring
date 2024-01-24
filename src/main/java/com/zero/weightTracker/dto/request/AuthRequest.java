package com.zero.weightTracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @NotBlank(message = "Ingresa un username válido.")
    @Size(min = 6, message = "El username debe tener al menos 6 caracteres.")
    private String username;

    @NotBlank
    @Size(min = 5, message = "La contraseña debe tener al menos 5 caracteres.")
    private String password;

}
