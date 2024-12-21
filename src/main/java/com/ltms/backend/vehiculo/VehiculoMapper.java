package com.ltms.backend.vehiculo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculoMapper {
    public Vehiculo convertirDtoAVehiculo(VehiculoDTO vehiculoDTO, List<String> imagenPaths) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(vehiculoDTO.getPlaca());
        vehiculo.setMarca(vehiculoDTO.getMarca());
        vehiculo.setTipo(vehiculoDTO.getTipo());
        vehiculo.setModelo(vehiculoDTO.getModelo());
        vehiculo.setAnio(vehiculoDTO.getAnio());
        vehiculo.setNumeroSerie(vehiculoDTO.getNumeroSerie());
        vehiculo.setImagenes(imagenPaths);
        vehiculo.setEstado(vehiculoDTO.getEstado());
        vehiculo.setProcedencia(vehiculoDTO.getProcedencia());
        return vehiculo;
    }

    public VehiculoDTOResponse vehiculoToDTOResponse(Vehiculo vehiculo){
        return VehiculoDTOResponse.builder()
                .idVehiculo(vehiculo.getId())
                .marca(vehiculo.getMarca())
                .placa(vehiculo.getPlaca())
                .anio(vehiculo.getAnio())
                .tipo(vehiculo.getTipo())
                .modelo(vehiculo.getModelo())
                .imagenes(vehiculo.getImagenes())
                .procedencia(vehiculo.getProcedencia())
                .numeroSerie(vehiculo.getNumeroSerie())
                .estado(vehiculo.getEstado())
                .build();
    }
}
