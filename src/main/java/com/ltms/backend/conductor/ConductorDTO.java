package com.ltms.backend.conductor;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ConductorDTO {
    // Datos del conductor
    private Integer idConductor;
    @NotEmpty(message = "El celular es obligatorio")
    @NotBlank(message = "El celular es obligatorio")
    private String licencia;
    @NotEmpty(message = "El celular es obligatorio")
    @NotBlank(message = "El celular es obligatorio")
    private String descripcion;
    private LocalDate fechaContrato;
    private String estado;
    // Datos de la persona
    private Integer idPersona;
    @NotEmpty(message = "Los nombres son obligatorios")
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;
    @NotEmpty(message = "El apellido paterno es obligatorio")
    @NotBlank(message = "El apellido paterno es obligatorio")
    private String paterno;
    @NotEmpty(message = "El apellido materno es obligatorio")
    @NotBlank(message = "El apellido materno es obligatorio")
    private String materno;
    @Min(value=0, message = "El ci es obligatorio")
    private Integer ci;
    private LocalDate fechaNac;
    @NotEmpty(message = "La direccion es obligatorio")
    @NotBlank(message = "La direccion es obligatorio")
    private String direccion;
    @NotEmpty(message = "El celular es obligatorio")
    @NotBlank(message = "El celular es obligatorio")
    private String celular;
    private byte[] profile;

    //Datos del base entity
//    private LocalDateTime createdDate;
//    private LocalDateTime lastModifiedDate;
//    private Integer createdBy;
//    private Integer lastModifiedBy;
}
