package com.ltms.backend.carroceria;

import com.ltms.backend.vehiculo.Vehiculo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CarroceriaDTO {

    @NotNull(message = "El codigo de carroceria no puede ser nulo")
    private String codigo;
    @Min(value=0, message = "La capacidad no puede ser nulo")
    private int capacidad;
    private LocalDate anioFab;
    @NotNull(message = "El tipo de carroceria no puede ser nulo")
    private Carroceria.TipoCarroceria tipo;
    @Min(value=0, message = "El id del vehiculo no puede ser nulo")
    private Integer idVehiculo;

}
