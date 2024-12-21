package com.ltms.backend.unidadTransporte;

import com.ltms.backend.vehiculo.Vehiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UnidadTransporteRepository extends JpaRepository<UnidadTransporte, Integer> {
    @Query(
            """
            SELECT ut FROM UnidadTransporte ut JOIN FETCH ut.conductor c JOIN FETCH c.persona p JOIN FETCH ut.vehiculo v
            """
    )
    Page<UnidadTransporte> findAllUnidadesTransporte(Pageable pageable);
}
