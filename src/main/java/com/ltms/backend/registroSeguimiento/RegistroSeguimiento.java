package com.ltms.backend.registroSeguimiento;

import com.ltms.backend.planillaSeguimiento.PlanillaSeguimiento;
import com.ltms.backend.producto.Producto;
import com.ltms.backend.ruta.Ruta;
import com.ltms.backend.unidadTransporte.UnidadTransporte;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RegistroSeguimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRegistroSeguimiento;
    private String ubicacion;
    private String estadoCarga;
    private LocalDate fechaArribo;
    private String horaLlegada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruta", nullable = false)
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad_transporte", nullable = false)
    private UnidadTransporte unidadTransporte;

    @ManyToMany(mappedBy = "registroSeguimiento")
    private Set<PlanillaSeguimiento> planillasSeguimiento = new HashSet<>();

}
