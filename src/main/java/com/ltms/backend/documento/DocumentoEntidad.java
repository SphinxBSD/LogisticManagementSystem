package com.ltms.backend.documento;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DocumentoEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDocumentoEntidad;

    @ManyToOne
    @JoinColumn(name = "id")
    private Documento documento;

    private Integer idEntidad;

    @Enumerated(EnumType.STRING)
    private TipoEntidad tipoEntidad;

    public enum TipoEntidad {
        VEHICULO,
        CONDUCTOR
    }
}

