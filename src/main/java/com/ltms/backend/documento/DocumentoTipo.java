package com.ltms.backend.documento;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DocumentoTipo {
    @Id
    @GeneratedValue
    private Integer idDocumentoTipo;
    private String nombreTipo;
    private String descripcion = "No tiene descripcion";
    private String dominio;
}
