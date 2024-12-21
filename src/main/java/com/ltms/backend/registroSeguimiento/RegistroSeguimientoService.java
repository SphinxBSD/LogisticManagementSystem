package com.ltms.backend.registroSeguimiento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistroSeguimientoService {
    private final RegistroSeguimientoRepository registroSeguimientoRepository;

    // Create
    public RegistroSeguimiento crear(RegistroSeguimiento registroSeguimiento) {
        return registroSeguimientoRepository.save(registroSeguimiento);
    }

    // Read All
    public List<RegistroSeguimiento> listarTodos() {
        return registroSeguimientoRepository.findAll();
    }

    // Read by ID
    public RegistroSeguimiento obtenerPorId(Integer id) {
        return registroSeguimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RegistroSeguimiento no encontrado con ID: " + id));
    }

    // Update
    public RegistroSeguimiento actualizar(Integer id, RegistroSeguimiento detalles) {
        RegistroSeguimiento registroExistente = obtenerPorId(id);

        registroExistente.setUbicacion(detalles.getUbicacion());
        registroExistente.setEstadoCarga(detalles.getEstadoCarga());
        registroExistente.setFechaArribo(detalles.getFechaArribo());
        registroExistente.setHoraLlegada(detalles.getHoraLlegada());
        registroExistente.setRuta(detalles.getRuta());
        registroExistente.setProducto(detalles.getProducto());
        registroExistente.setUnidadTransporte(detalles.getUnidadTransporte());

        return registroSeguimientoRepository.save(registroExistente);
    }

    // Delete
    public void eliminar(Integer id) {
        RegistroSeguimiento registro = obtenerPorId(id);
        registroSeguimientoRepository.delete(registro);
    }
}
