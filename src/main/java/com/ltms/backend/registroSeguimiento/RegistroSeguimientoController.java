package com.ltms.backend.registroSeguimiento;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("registro-seg")
@RequiredArgsConstructor
public class RegistroSeguimientoController {
    private final RegistroSeguimientoService registroSeguimientoService;

    // Create
    @PostMapping("/crear")
    public ResponseEntity<RegistroSeguimiento> crear(@RequestBody RegistroSeguimiento registroSeguimiento) {
        return ResponseEntity.ok(registroSeguimientoService.crear(registroSeguimiento));
    }

    // Read All
    @GetMapping("/listar")
    public ResponseEntity<List<RegistroSeguimiento>> listarTodos() {
        return ResponseEntity.ok(registroSeguimientoService.listarTodos());
    }

    // Read by ID
    @GetMapping("/obtener/{id}")
    public ResponseEntity<RegistroSeguimiento> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(registroSeguimientoService.obtenerPorId(id));
    }

    // Update
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<RegistroSeguimiento> actualizar(@PathVariable Integer id, @RequestBody RegistroSeguimiento detalles) {
        return ResponseEntity.ok(registroSeguimientoService.actualizar(id, detalles));
    }

    // Delete
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        registroSeguimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
