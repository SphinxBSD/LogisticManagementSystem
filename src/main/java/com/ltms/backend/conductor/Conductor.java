package com.ltms.backend.conductor;

import com.ltms.backend.common.BaseEntity;
import com.ltms.backend.persona.Persona;
import com.ltms.backend.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Conductor extends BaseEntity {

    private String licencia;
    private String descripcion;
    private LocalDate fechaContrato;
    private String estado;

    @OneToOne
    @JoinColumn(name = "id_persona", referencedColumnName = "idPersona")
    private Persona persona;

}
