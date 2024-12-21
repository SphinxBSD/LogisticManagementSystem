package com.ltms.backend.ruta;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("rutas")
public class RutaController {
    private final RutaService rutaService;

    @GetMapping
    public ResponseEntity<List<Ruta>> listarRutas() {
        List<Ruta> rutas = rutaService.obtenerTodasLasRutas();
        return ResponseEntity.ok(rutas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ruta> obtenerRuta(@PathVariable Integer id) {
        return rutaService.obtenerRutaPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalStateException("Ruta no encontrada con id " + id));
    }

    @PostMapping
    public ResponseEntity<Ruta> crearRuta(@RequestBody Ruta ruta) {
        Ruta nuevaRuta = rutaService.crearRuta(ruta);
        return ResponseEntity.ok(nuevaRuta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ruta> actualizarRuta(@PathVariable Integer id, @RequestBody Ruta detallesRuta) {
        Ruta rutaActualizada = rutaService.actualizarRuta(id, detallesRuta);
        return ResponseEntity.ok(rutaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRuta(@PathVariable Integer id) {
        rutaService.eliminarRuta(id);
        return ResponseEntity.noContent().build();
    }
}
