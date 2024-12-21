package com.ltms.backend.unidadTransporte;

import com.ltms.backend.common.PageResponse;
import com.ltms.backend.vehiculo.Vehiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("unidad-transporte")
public class UnidadTransporteController {
    private final UnidadTransporteService unidadTransporteService;

    @GetMapping
    public List<UnidadTransporte> getAll() {
        return unidadTransporteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadTransporte> getById(@PathVariable Integer id) {
        return unidadTransporteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UnidadTransporte create(@RequestBody UnidadTransporte unidadTransporte) {
        return unidadTransporteService.save(unidadTransporte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadTransporte> update(@PathVariable Integer id, @RequestBody UnidadTransporte unidadTransporte) {
        return unidadTransporteService.findById(id)
                .map(existing -> {
                    unidadTransporte.setIdUnidadTransporte(id);
                    return ResponseEntity.ok(unidadTransporteService.save(unidadTransporte));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (unidadTransporteService.findById(id).isPresent()) {
            unidadTransporteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/showAll")
    public ResponseEntity<PageResponse<UnidadTransporteDTO>> listarUnidadesTransporte(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size,
            @RequestParam(name = "sort", defaultValue = "fechaInicio", required = false) String sort,
            @RequestParam(name = "order", defaultValue = "desc", required = false) String order
    ) {
        return ResponseEntity.ok(unidadTransporteService.listarUnidadesTransporteDTO(page, size, sort, order));
    }

}
