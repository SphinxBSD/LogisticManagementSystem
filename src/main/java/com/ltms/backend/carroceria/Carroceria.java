package com.ltms.backend.carroceria;

import com.ltms.backend.vehiculo.Vehiculo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Carroceria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarroceria;

    @Column(unique = true)
    private String codigo;
    private int capacidad;
    private LocalDate anioFab;

    @Enumerated(EnumType.STRING)
    private Carroceria.TipoCarroceria tipo;

    @OneToOne
    @JoinColumn(name = "id_vehiculo", referencedColumnName = "id")
    private Vehiculo vehiculo;

    @ElementCollection
    @CollectionTable(name = "carroceria_imagenes", joinColumns = @JoinColumn(name = "id_carroceria"))
    @Column(name = "imagen_path")
    private List<String> imagenes;

    public enum TipoCarroceria {
        VOLTEO,
        TANQUE,
        PORTACONTENEDORES,
        GRUA,
        TOLVA
    }
}
