package com.ltms.backend.usuario;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioRegistroResponse {
    private String message;
    private Integer idUsuario;

    public UsuarioRegistroResponse(String message, Integer idUsuario) {
        this.message = message;
        this.idUsuario = idUsuario;
    }
}
