package com.ltms.backend.documento;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DocumentoDTO {
    private Integer idDocumento;

    @NotNull(message = "El tipo de documento es obligatorio")
    private Integer idDocumentoTipo;

    private LocalDate fechaVencimiento;

    @NotNull(message = "El estado del documento es obligatorio")
    private String estadoDocumento;

    @NotNull(message = "El ID de la entidad es obligatorio")
    private Integer idEntidad;

    @NotNull(message = "El tipo de entidad es obligatorio")
    private DocumentoEntidad.TipoEntidad tipoEntidad;

    private String rutaArchivo;
}
