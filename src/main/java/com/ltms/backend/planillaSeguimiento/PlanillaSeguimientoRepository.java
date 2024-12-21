package com.ltms.backend.planillaSeguimiento;

import com.ltms.backend.registroSeguimiento.RegistroSeguimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanillaSeguimientoRepository extends JpaRepository<PlanillaSeguimiento, Integer> {

}
