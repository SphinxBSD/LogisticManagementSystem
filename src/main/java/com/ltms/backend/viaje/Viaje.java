package com.ltms.backend.viaje;

import com.ltms.backend.programa.Programa;
import com.ltms.backend.unidadTransporte.UnidadTransporte;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idViaje;
    private LocalDate fechaAsignacion;
    private String observacion;
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad_transporte", nullable = false)
    private UnidadTransporte unidadTransporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_programa", nullable = false)
    private Programa programa;

}
