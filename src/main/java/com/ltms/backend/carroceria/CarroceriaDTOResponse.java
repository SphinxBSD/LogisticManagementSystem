package com.ltms.backend.carroceria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class CarroceriaDTOResponse {

    private Integer idCarroceria;
    private String codigo;
    private int capacidad;
    private LocalDate anioFab;
    private Carroceria.TipoCarroceria tipo;
    private List<String> imagenes;
}
