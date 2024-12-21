package com.ltms.backend.vehiculo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    Optional<Vehiculo> findByPlaca(String placa);

    @Query(
            """
            SELECT vehiculo
            FROM Vehiculo vehiculo
            WHERE vehiculo.estado = 'ACTIVO'
            """
    )
    Page<Vehiculo> findAllVehiculos(Pageable pageable);

    @Query("SELECT new com.ltms.backend.vehiculo.VehiculoListaDTO(v.id, v.placa) FROM Vehiculo v")
    List<VehiculoListaDTO> findAllVehiculoDTO();
}
