package com.ltms.backend.carroceria;

import com.ltms.backend.file.FileStorageService;
import com.ltms.backend.vehiculo.Vehiculo;
import com.ltms.backend.vehiculo.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarroceriaService {
    private final CarroceriaRepository carroceriaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final FileStorageService fileStorageService;
    private final CarroceriaMapper carroceriaMapper;

    private final String uploadDir = "vehiculo/";

    public Carroceria guardarCarroceria(Carroceria carroceria) {return carroceriaRepository.save(carroceria);}

    @Transactional
    public Carroceria guardarCarroceriacConImagenes(
            CarroceriaDTO carroceriaDTO,
            List<MultipartFile> imagenes
    ) throws IOException {
        if (imagenes.size() < 3) {
            throw new IllegalArgumentException("Se requieren al menos 3 imágenes");
        }

        Vehiculo vehiculo;
        Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findById(carroceriaDTO.getIdVehiculo());
        if (vehiculoOpt.isPresent()){
            vehiculo = vehiculoOpt.get();
        }else {
            throw new IllegalStateException("Vehiculo no encontrado");
        }

        List<String> imagenNames = new ArrayList<>();
        for (MultipartFile imagen: imagenes){
            String imagenName = fileStorageService.saveFileV2(imagen, uploadDir + carroceriaDTO.getCodigo());
            imagenNames.add(imagenName);
        }
        Carroceria carroceria = carroceriaMapper.carroceriaDtoToCarroceria(carroceriaDTO, imagenNames, vehiculo);
        return  guardarCarroceria(carroceria);
    }

    @Transactional
    public CarroceriaDTOResponse obtenerCarroceriaPorId(Integer idVehiculo) {
        Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findById(idVehiculo);
        if (vehiculoOpt.isPresent()){
            Optional<Carroceria> carroceriaOpt = carroceriaRepository.findCarroceriaByVehiculo(vehiculoOpt.get());
            if (carroceriaOpt.isPresent()){
                Carroceria carroceria = carroceriaOpt.get();
                return carroceriaMapper.carroceriaToCarroceriaDTO(carroceria);
            }else {
                return new CarroceriaDTOResponse(0,"",0,null,null, null);
//                throw new IllegalStateException("Carroceria no encontrada");
            }
        }else{
            return new CarroceriaDTOResponse(0,"",0,null,null, null);
//            throw new IllegalStateException("Vehiculo no encontrado");
        }
    }
}
