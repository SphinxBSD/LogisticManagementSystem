package com.ltms.backend.documento;

import com.ltms.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Documento extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "id_documento_tipo", referencedColumnName = "idDocumentoTipo")
    private DocumentoTipo documentoTipo;

    private String rutaArchivo;
    private LocalDate fechaVencimiento;
    private String estadoDocumento;
}
