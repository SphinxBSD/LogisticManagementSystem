package com.ltms.backend.vehiculo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltms.backend.carroceria.Carroceria;
import com.ltms.backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vehiculo extends BaseEntity {
    @Column(unique = true)
    private String placa;
    private String marca;
    @Enumerated(EnumType.STRING)
    private TipoVehiculo tipo;
    private String modelo;
    private int anio;
    private String numeroSerie;
    private String procedencia;
    private String estado = "ACTIVO";
    @OneToOne(mappedBy = "vehiculo", cascade = CascadeType.ALL)
    @JsonIgnore
    private Carroceria carroceria;

    @ElementCollection
    @CollectionTable(name = "vehiculo_imagenes", joinColumns = @JoinColumn(name = "id_vehiculo"))
    @Column(name = "imagen_path")
    private List<String> imagenes;

    public enum TipoVehiculo {
        CAMION,
        FURGONETA,
        REMOLQUE,
        CISTERNA,
        CAMIONETA
    }
}
