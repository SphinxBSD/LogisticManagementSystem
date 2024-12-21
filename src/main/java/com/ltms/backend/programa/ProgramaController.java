package com.ltms.backend.programa;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("programa")
public class ProgramaController {
    private final ProgramaService programaService;

    @GetMapping
    public ResponseEntity<List<Programa>> listarProgramas() {
        List<Programa> programas = programaService.obtenerTodosLosProgramas();
        return ResponseEntity.ok(programas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Programa> obtenerPrograma(@PathVariable Integer id) {
        return programaService.obtenerProgramaPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalStateException("Programa no encontrado con id " + id));
    }

    @PostMapping
    public ResponseEntity<Programa> crearPrograma(@RequestBody Programa programa) {
        Programa nuevoPrograma = programaService.crearPrograma(programa);
        return ResponseEntity.ok(nuevoPrograma);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Programa> actualizarPrograma(@PathVariable Integer id, @RequestBody Programa detallesPrograma) {
        Programa programaActualizado = programaService.actualizarPrograma(id, detallesPrograma);
        return ResponseEntity.ok(programaActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrograma(@PathVariable Integer id) {
        programaService.eliminarPrograma(id);
        return ResponseEntity.noContent().build();
    }
}
