package com.ltms.backend.planillaSeguimiento;

import com.ltms.backend.registroSeguimiento.RegistroSeguimiento;
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
public class PlanillaSeguimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPlanillaSeguimiento;
    private String turno;
    private LocalDate fechaPlanilla;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "planilla_registro",
            joinColumns = @JoinColumn(name = "id_planilla_seguimiento"),
            inverseJoinColumns = @JoinColumn(name = "id_registro_seguimiento")
    )
    private Set<RegistroSeguimiento> registrosSeguimiento = new HashSet<>();

    // Método para agregar un registro de seguimiento
    public void addRegistroSeguimiento(RegistroSeguimiento registro) {
        this.registrosSeguimiento.add(registro);
        registro.getPlanillasSeguimiento().add(this);
    }

    // Método para eliminar un registro de seguimiento
    public void removeRegistroSeguimiento(RegistroSeguimiento registro) {
        this.registrosSeguimiento.remove(registro);
        registro.getPlanillasSeguimiento().remove(this);
    }
}
