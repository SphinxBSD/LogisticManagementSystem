package com.ltms.backend.vehiculo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VehiculoListaDTO {
    private Integer idVehiculo;
    private String placa;
}
