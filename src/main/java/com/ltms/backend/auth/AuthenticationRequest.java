package com.ltms.backend.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @NotEmpty(message = "El username es obligatorio")
    @NotNull(message = "El username es obligatorio")
    private String username;

    @NotEmpty(message = "El password es obligatorio")
    @NotNull(message = "El password es obligatorio")
    @Size(min = 6, message = "El password debe tener 6 caracteres minimo")
    private String password;
}
