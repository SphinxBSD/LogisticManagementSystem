package com.ltms.backend.usuario;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UsuarioResponse {
    private Integer idUsuario;
    private String username;
    private String email;
    private Boolean enabled;

    private String nombres;
    private String paterno;
    private String materno;
    private String celular;
    private String direccion;
    private String estado;
    private String rol;
    private Integer ci;
    private LocalDate fechaNac;
    private byte[] profileImage;

}
