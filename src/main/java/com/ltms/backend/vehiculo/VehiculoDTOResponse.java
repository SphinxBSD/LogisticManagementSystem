package com.ltms.backend.vehiculo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class VehiculoDTOResponse {
    private Integer idVehiculo;
    private String placa;
    private String marca;
    private Vehiculo.TipoVehiculo tipo;
    private String modelo;
    private Integer anio;
    private String numeroSerie;
    private String estado;
    private String procedencia;
    private List<String> imagenes;
}
