package com.ltms.backend.carroceria;

import com.ltms.backend.vehiculo.Vehiculo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarroceriaMapper {
    public Carroceria carroceriaDtoToCarroceria(
            CarroceriaDTO carroceriaDTO, List<String> imagenPaths, Vehiculo vehiculo
    ){
        Carroceria carroceria = new Carroceria();
        carroceria.setCodigo(carroceriaDTO.getCodigo());
        carroceria.setTipo(carroceriaDTO.getTipo());
        carroceria.setAnioFab(carroceriaDTO.getAnioFab());
        carroceria.setVehiculo(vehiculo);
        carroceria.setCapacidad(carroceriaDTO.getCapacidad());
        carroceria.setImagenes(imagenPaths);

        return carroceria;
    }

    public CarroceriaDTOResponse carroceriaToCarroceriaDTO(
        Carroceria carroceria
    ){
        return CarroceriaDTOResponse.builder()
                .idCarroceria(carroceria.getIdCarroceria())
                .anioFab(carroceria.getAnioFab())
                .capacidad(carroceria.getCapacidad())
                .tipo(carroceria.getTipo())
                .codigo(carroceria.getCodigo())
                .imagenes(carroceria.getImagenes())
                .build();
    }
}
