package com.ltms.backend.documento;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DocumentoMapper {
    private final DocumentoEntidadRepository documentoEntidadRepository;

    /**
     * Convierte una entidad Documento a DocumentoDTO.
     */
    public DocumentoDTO documentoToDTO(Documento documento) {


//        DocumentoDTO dto = new DocumentoDTO();
//        dto.setId(documento.getId());
//        dto.setIdDocumentoTipo(documento.getDocumentoTipo().getId());
//        dto.setFechaVencimiento(documento.getFechaVencimiento());
//        dto.setEstadoDocumento(documento.getEstadoDocumento());
//        dto.setRutaArchivo(documento.getRutaArchivo());


        // Obtener la relación con la entidad
        DocumentoEntidad documentoEntidad = documentoEntidadRepository.findByDocumento(documento)
                .orElseThrow(() -> new IllegalStateException("Relación DocumentoEntidad no encontrada"));

        return DocumentoDTO.builder()
                .idDocumento(documento.getId())
                .idDocumentoTipo(documento.getDocumentoTipo().getIdDocumentoTipo())
                .fechaVencimiento(documento.getFechaVencimiento())
                .estadoDocumento(documento.getEstadoDocumento())
                .rutaArchivo(documento.getRutaArchivo())
                .idEntidad(documentoEntidad.getIdEntidad())
                .tipoEntidad(documentoEntidad.getTipoEntidad())
                .build();
    }
}
