package com.ltms.backend.vehiculo;

import com.ltms.backend.common.PageResponse;
import com.ltms.backend.file.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final VehiculoMapper vehiculoMapper;
    private final FileStorageService fileStorageService;

    private final String uploadDir = "vehiculo/";
    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public Optional<Vehiculo> buscarVehiculoPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa);
    }

    @Transactional
    public PageResponse<Vehiculo> listarVehiculos(
            int page,
            int size,
            String sort,
            String order
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Vehiculo> vehiculos = vehiculoRepository.findAllVehiculos(pageable);
        List<Vehiculo> vehiculoList = vehiculos.stream().toList();
        return new PageResponse<>(
                vehiculoList,
                vehiculos.getNumber(),
                vehiculos.getSize(),
                vehiculos.getTotalElements(),
                vehiculos.getTotalPages(),
                vehiculos.isFirst(),
                vehiculos.isLast());
//        return vehiculoRepository.findAll();
    }

    public void eliminarVehiculo(Integer id) {
        vehiculoRepository.deleteById(id);
    }

    @Transactional
    public Vehiculo guardarVehiculoConImagenes(
            VehiculoDTO vehiculoDTO, List<MultipartFile> imagenes) throws IOException {
        if (imagenes.size() < 3) {
            throw new IllegalArgumentException("Se requieren al menos 3 imágenes");
        }

        if (vehiculoRepository.findByPlaca(vehiculoDTO.getPlaca()).isPresent()){
            throw new RuntimeException("El vehiculo con la placa: "+vehiculoDTO.getPlaca()+" ya fue creado");
        }

        List<String> imagenNames = new ArrayList<>();
        for (MultipartFile imagen : imagenes) {
            // Guardar la imagen en el sistema de archivos (ejemplo simplificado)
            String imagenName = fileStorageService.saveFileV2(imagen, uploadDir + vehiculoDTO.getPlaca());
            imagenNames.add(imagenName);
        }
        Vehiculo vehiculo = vehiculoMapper.convertirDtoAVehiculo(vehiculoDTO, imagenNames);
        return guardarVehiculo(vehiculo);
    }


    @Transactional
    public VehiculoDTOResponse obtenerVehiculoPorId(Integer idVehiculo){
        Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findById(idVehiculo);
        if (vehiculoOpt.isPresent()){
            Vehiculo vehiculo = vehiculoOpt.get();
            return vehiculoMapper.vehiculoToDTOResponse(vehiculo);
        }else{
            throw new IllegalStateException("Vehiculo no encontrado");
        }
    }

    @Transactional
    public List<VehiculoListaDTO> obtenerTodosLosVehiculos() {
        return vehiculoRepository.findAllVehiculoDTO();
    }

}
