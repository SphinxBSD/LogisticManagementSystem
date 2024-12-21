package com.ltms.backend.carroceria;

import com.ltms.backend.common.MessageResponse;
import com.ltms.backend.file.FileStorageService;
import com.ltms.backend.vehiculo.VehiculoDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("carroceria")
@RequiredArgsConstructor
public class CarroceriaController {
    private final CarroceriaService carroceriaService;
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/guardar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MessageResponse> guardarCarroceria(
            @ModelAttribute @Valid CarroceriaDTO carroceriaDTO,
            @RequestPart("imagenes")List<MultipartFile> imagenes
            ){
        try{
            Carroceria carroceriaSaved = carroceriaService.guardarCarroceriacConImagenes(carroceriaDTO, imagenes);
            MessageResponse messageResponse = new MessageResponse("La carroceria se guardo exitosamente", carroceriaSaved.getIdCarroceria());
            return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getinfo/{idVehiculo}")
    public ResponseEntity<CarroceriaDTOResponse> obtenerCarroceriaPorId(@PathVariable Integer idVehiculo) {
        CarroceriaDTOResponse carroceriaDTOResponse = carroceriaService.obtenerCarroceriaPorId(idVehiculo);
        return ResponseEntity.ok(carroceriaDTOResponse);
    }

    //    CONTROLADOR PARA SERVIR LAS IMAGENES
    @GetMapping("/imagenes/{codigo}/{nombreImagen}")
    public ResponseEntity<Resource> obtenerImagen(
            @PathVariable String codigo,
            @PathVariable String nombreImagen) {
        try {
            // Validar que el nombre de la imagen no contenga caracteres peligrosos
            if (nombreImagen.contains("..") || nombreImagen.contains("/")) {
                throw new SecurityException("Nombre de archivo inválido");
            }

            Path rutaImagen = Paths.get("uploads/vehiculo/" + codigo + "/" + nombreImagen).normalize();
//            System.out.println(rutaImagen);
            Resource recurso = new UrlResource(rutaImagen.toUri());

            if (recurso.exists() || recurso.isReadable()) {
                String contentType = Files.probeContentType(rutaImagen);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
