package com.ltms.backend.ruta;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RutaService {
    private final RutaRepository rutaRepository;

    public List<Ruta> obtenerTodasLasRutas() {
        return rutaRepository.findAll();
    }

    public Optional<Ruta> obtenerRutaPorId(Integer id) {
        return rutaRepository.findById(id);
    }

    public Ruta crearRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public Ruta actualizarRuta(Integer id, Ruta detallesRuta) {
        return rutaRepository.findById(id)
                .map(ruta -> {
                    ruta.setOrigen(detallesRuta.getOrigen());
                    ruta.setFrontera(detallesRuta.getFrontera());
                    ruta.setDestino(detallesRuta.getDestino());
                    return rutaRepository.save(ruta);
                })
                .orElseThrow(() -> new IllegalStateException("Ruta no encontrada con id " + id));
    }

    public void eliminarRuta(Integer id) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Ruta no encontrada con id " + id));
        rutaRepository.delete(ruta);
    }
}
