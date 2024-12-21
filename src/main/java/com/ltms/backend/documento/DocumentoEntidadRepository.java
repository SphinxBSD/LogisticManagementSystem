package com.ltms.backend.documento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentoEntidadRepository extends JpaRepository<DocumentoEntidad, Integer> {
    List<DocumentoEntidad> findByIdEntidadAndTipoEntidad(Integer idEntidad, DocumentoEntidad.TipoEntidad tipoEntidad);

    Optional<DocumentoEntidad> findByDocumento(Documento documento);

    void deleteById(Integer idDocumento);
}
