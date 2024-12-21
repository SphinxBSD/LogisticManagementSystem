package com.ltms.backend.carroceria;

import com.ltms.backend.vehiculo.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarroceriaRepository extends JpaRepository<Carroceria, Integer> {

    Optional<Carroceria> findCarroceriaByVehiculo(Vehiculo vehiculo);

}
