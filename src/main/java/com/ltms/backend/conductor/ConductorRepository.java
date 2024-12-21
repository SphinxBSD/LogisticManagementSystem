package com.ltms.backend.conductor;

import com.ltms.backend.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConductorRepository extends JpaRepository<Conductor, Integer> {
    Optional<Conductor> findById(Integer idConductor);

    @Query(
            """
            SELECT conductor
            FROM Conductor conductor
            WHERE conductor.persona.estado = 'activo'
            """
    )
    Page<Conductor> findAllConductores(Pageable pageable);

    @Query(
            "SELECT new com.ltms.backend.conductor.ConductorListaDTO(c.id, CONCAT(p.nombres, ' ', p.paterno, ' ', p.materno)) " +
                    "FROM Conductor c INNER JOIN c.persona p"
    )
    List<ConductorListaDTO> findAllConductorDTO();
}
