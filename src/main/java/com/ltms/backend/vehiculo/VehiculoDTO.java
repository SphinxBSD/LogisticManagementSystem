package com.ltms.backend.vehiculo;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class VehiculoDTO {

    @NotNull(message = "La placa no puede ser nula")
    private String placa;

    @NotNull(message = "La marca no puede ser nula")
    private String marca;

    @NotNull(message = "El tipo no puede ser nulo")
    private Vehiculo.TipoVehiculo tipo;

    @NotNull(message = "El modelo no puede ser nulo")
    private String modelo;

    @NotNull(message = "El año no puede ser nulo")
    private Integer anio;

    @NotNull(message = "El número de serie no puede ser nulo")
    private String numeroSerie;

    @NotNull(message = "El estado no puede ser nulo")
    private String estado;

    @NotNull(message = "La procedencia no puede ser nulo")
    private String procedencia;
}
