package com.ltms.backend.programa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgramaService {
    private final ProgramaRepository programaRepository;

    public List<Programa> obtenerTodosLosProgramas() {
        return programaRepository.findAll();
    }

    public Optional<Programa> obtenerProgramaPorId(Integer id) {
        return programaRepository.findById(id);
    }

    public Programa crearPrograma(Programa programa) {
        return programaRepository.save(programa);
    }

    public Programa actualizarPrograma(Integer id, Programa detallesPrograma) {
        return programaRepository.findById(id)
                .map(programa -> {
                    programa.setFechaIni(detallesPrograma.getFechaIni());
                    programa.setFechaFin(detallesPrograma.getFechaFin());
                    programa.setVolumen(detallesPrograma.getVolumen());
                    programa.setProducto(detallesPrograma.getProducto());
                    programa.setRuta(detallesPrograma.getRuta());
                    return programaRepository.save(programa);
                })
                .orElseThrow(() -> new IllegalStateException("Programa no encontrado con id " + id));
    }

    public void eliminarPrograma(Integer id) {
        Programa programa = programaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Programa no encontrado con id " + id));
        programaRepository.delete(programa);
    }
}
