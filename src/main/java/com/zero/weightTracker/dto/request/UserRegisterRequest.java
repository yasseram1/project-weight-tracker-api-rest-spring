package com.zero.weightTracker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {

    @NotBlank(message = "Ingresa un username válido.")
    @Size(min = 6, message = "El username debe tener al menos 6 caracteres.")
    private String username;

    @Email
    @NotBlank(message = "Ingresa un correo electrónico válido.")
    private String email;

    @NotBlank
    @Size(min = 5, message = "La contraseña debe tener al menos 5 caracteres.")
    private String password;

}
