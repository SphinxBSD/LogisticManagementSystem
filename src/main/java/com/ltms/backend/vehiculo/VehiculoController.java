package com.ltms.backend.vehiculo;

import com.ltms.backend.common.MessageResponse;
import com.ltms.backend.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import java.util.Optional;

@RestController
@RequestMapping("vehiculo")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping(value = "/registrar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MessageResponse> crearVehiculo(@ModelAttribute @Valid VehiculoDTO vehiculoDTO,
                                                         @RequestPart("imagenes") List<MultipartFile> imagenes) {
        try {
            Vehiculo vehiculoGuardado = vehiculoService.guardarVehiculoConImagenes(vehiculoDTO, imagenes);
            MessageResponse messageResponse = new MessageResponse("El vehiculo fue creado con exito!", vehiculoGuardado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getinfo/{idVehiculo}")
    public ResponseEntity<VehiculoDTOResponse> obtenerVehiculoPorId(@PathVariable Integer idVehiculo) {
        VehiculoDTOResponse vehiculoDTOResponse = vehiculoService.obtenerVehiculoPorId(idVehiculo);
        return ResponseEntity.ok(vehiculoDTOResponse);
    }

    //    CONTROLADOR PARA SERVIR LAS IMAGENES
    @GetMapping("/imagenes/{placa}/{nombreImagen}")
    public ResponseEntity<Resource> obtenerImagen(
            @PathVariable String placa,
            @PathVariable String nombreImagen) {

        try {
            // Validar que el nombre de la imagen no contenga caracteres peligrosos
            if (nombreImagen.contains("..") || nombreImagen.contains("/")) {
                throw new SecurityException("Nombre de archivo inválido");
            }

            Path rutaImagen = Paths.get("uploads/vehiculo/" + placa + "/" + nombreImagen).normalize();
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

    @GetMapping("/obtener/{placa}")
    public ResponseEntity<Vehiculo> obtenerVehiculoPorPlaca(@PathVariable String placa) {
        Optional<Vehiculo> vehiculoOpt = vehiculoService.buscarVehiculoPorPlaca(placa);
        return vehiculoOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/showAllVehiculos")
    public ResponseEntity<PageResponse<Vehiculo>> listarVehiculos(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size,
            @RequestParam(name = "sort", defaultValue = "createdDate", required = false) String sort,
            @RequestParam(name = "order", defaultValue = "desc", required = false) String order
    ) {
        return ResponseEntity.ok(vehiculoService.listarVehiculos(page, size, sort, order));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable Integer id) {
        vehiculoService.eliminarVehiculo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mostrar-basic")
    public List<VehiculoListaDTO> mostrarVehiculosBasic() {
        return vehiculoService.obtenerTodosLosVehiculos();
    }

}
