package com.ltms.backend.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageResponse {
    private String message;
    private Integer idEntidad;

    public MessageResponse(String message, Integer idEntidad) {
        this.message = message;
        this.idEntidad = idEntidad;
    }
}
