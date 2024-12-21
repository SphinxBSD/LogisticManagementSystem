package com.ltms.backend.unidadTransporte;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UnidadTransporteDTO {
    private Integer idUnidadTransporte;
    private LocalDate fechaInicio;
    private String estado;
    private String fullnameConductor;
    private String placaVehiculo;
    private Integer idConductor;
    private Integer idVehiculo;
}
