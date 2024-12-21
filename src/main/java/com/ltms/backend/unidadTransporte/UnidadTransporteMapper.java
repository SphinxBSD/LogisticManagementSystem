package com.ltms.backend.unidadTransporte;

import org.springframework.stereotype.Service;

@Service
public class UnidadTransporteMapper {

    public UnidadTransporteDTO unidadTransporteToDTO(UnidadTransporte unidadTransporte){
        return UnidadTransporteDTO.builder()
                .idUnidadTransporte(unidadTransporte.getIdUnidadTransporte())
                .fullnameConductor(
                        unidadTransporte.getConductor().getPersona().getNombres() + " " +
                        unidadTransporte.getConductor().getPersona().getPaterno() + " " +
                                unidadTransporte.getConductor().getPersona().getMaterno())
                .fechaInicio(unidadTransporte.getFechaInicio())
                .placaVehiculo(unidadTransporte.getVehiculo().getPlaca())
                .estado(unidadTransporte.getEstado())
                .idConductor(unidadTransporte.getConductor().getId())
                .idVehiculo(unidadTransporte.getVehiculo().getId())
                .build();
    }
}
