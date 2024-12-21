package com.ltms.backend.planillaSeguimiento;

import com.ltms.backend.registroSeguimiento.RegistroSeguimiento;
import com.ltms.backend.registroSeguimiento.RegistroSeguimientoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanillaSeguimientoService {
    private final PlanillaSeguimientoRepository planillaSeguimientoRepository;
    private final RegistroSeguimientoRepository registroSeguimientoRepository;

    // Crear una nueva Planilla de Seguimiento
    public PlanillaSeguimiento crearPlanillaSeguimiento(PlanillaSeguimiento planilla) {
        return planillaSeguimientoRepository.save(planilla);
    }

    // Obtener todas las Planillas de Seguimiento
    public List<PlanillaSeguimiento> obtenerTodasPlanillas() {
        return planillaSeguimientoRepository.findAll();
    }

    // Obtener Planilla de Seguimiento por ID
    public PlanillaSeguimiento obtenerPlanillaPorId(Integer id) {
        return planillaSeguimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Planilla no encontrada"));
    }

    // Actualizar Planilla de Seguimiento
    public PlanillaSeguimiento actualizarPlanilla(Integer id, PlanillaSeguimiento planillaActualizada) {
        PlanillaSeguimiento planillaExistente = obtenerPlanillaPorId(id);

        planillaExistente.setTurno(planillaActualizada.getTurno());
        planillaExistente.setFechaPlanilla(planillaActualizada.getFechaPlanilla());

        return planillaSeguimientoRepository.save(planillaExistente);
    }

    // Eliminar Planilla de Seguimiento
    public void eliminarPlanilla(Integer id) {
        PlanillaSeguimiento planilla = obtenerPlanillaPorId(id);
        planillaSeguimientoRepository.delete(planilla);
    }

    // Agregar un Registro de Seguimiento a una Planilla
    public PlanillaSeguimiento agregarRegistroASeguimiento(Integer idPlanilla, Integer idRegistro) {
        PlanillaSeguimiento planilla = obtenerPlanillaPorId(idPlanilla);
        RegistroSeguimiento registro = registroSeguimientoRepository.findById(idRegistro)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado"));

        planilla.addRegistroSeguimiento(registro);
        return planillaSeguimientoRepository.save(planilla);
    }

    // Eliminar un Registro de Seguimiento de una Planilla
    public PlanillaSeguimiento eliminarRegistroDeSeguimiento(Integer idPlanilla, Integer idRegistro) {
        PlanillaSeguimiento planilla = obtenerPlanillaPorId(idPlanilla);
        RegistroSeguimiento registro = registroSeguimientoRepository.findById(idRegistro)
                .orElseThrow(() -> new EntityNotFoundException("Registro no encontrado"));

        planilla.removeRegistroSeguimiento(registro);
        return planillaSeguimientoRepository.save(planilla);
    }
}
