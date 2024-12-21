package com.ltms.backend.unidadTransporte;

import com.ltms.backend.common.PageResponse;
import com.ltms.backend.vehiculo.Vehiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnidadTransporteService {

    private final UnidadTransporteRepository unidadTransporteRepository;
    private final UnidadTransporteMapper unidadTransporteMapper;
    public List<UnidadTransporte> findAll() {
        return unidadTransporteRepository.findAll();
    }

    public Optional<UnidadTransporte> findById(Integer id) {
        return unidadTransporteRepository.findById(id);
    }

    public UnidadTransporte save(UnidadTransporte unidadTransporte) {
        return unidadTransporteRepository.save(unidadTransporte);
    }

    public void deleteById(Integer id) {
        unidadTransporteRepository.deleteById(id);
    }

    @Transactional
    public PageResponse<UnidadTransporteDTO> listarUnidadesTransporteDTO(
            int page,
            int size,
            String sort,
            String order
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<UnidadTransporte> unidadesTransporte = unidadTransporteRepository.findAllUnidadesTransporte(pageable);
        List<UnidadTransporteDTO> unidadesTransporteList = unidadesTransporte.stream()
                .map(unidadTransporteMapper::unidadTransporteToDTO)
                .toList();
        return new PageResponse<>(
                unidadesTransporteList,
                unidadesTransporte.getNumber(),
                unidadesTransporte.getSize(),
                unidadesTransporte.getTotalElements(),
                unidadesTransporte.getTotalPages(),
                unidadesTransporte.isFirst(),
                unidadesTransporte.isLast());
    }

}
