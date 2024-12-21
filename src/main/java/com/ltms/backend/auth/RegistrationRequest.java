package com.ltms.backend.auth;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "El username es obligatorio")
    @NotBlank(message = "El username es obligatorio")
    private String username;
    @NotEmpty(message = "El password es obligatorio")
    @NotBlank(message = "El password es obligatorio")
    @Size(min = 8, message = "El password debe tener 8 caracteres minimo")
    private String password;
    @Email(message = "Formato email no valido")
    @NotEmpty(message = "El email es obligatorio")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotEmpty(message = "Los nombres son obligatorios")
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;
    @NotEmpty(message = "El apellido paterno es obligatorio")
    @NotBlank(message = "El apellido paterno es obligatorio")
    private String paterno;
    @NotEmpty(message = "El apellido materno es obligatorio")
    @NotBlank(message = "El apellido materno es obligatorio")
    private String materno;
    @Min(value=0, message = "El ci es obligatorio")
    private Integer ci;
    private LocalDate fechaNac;
    @NotEmpty(message = "La direccion es obligatorio")
    @NotBlank(message = "La direccion es obligatorio")
    private String direccion;
    @NotEmpty(message = "El celular es obligatorio")
    @NotBlank(message = "El celular es obligatorio")
    private String celular;

}
