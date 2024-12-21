package com.ltms.backend.conductor;

import com.ltms.backend.common.PageResponse;
import com.ltms.backend.usuario.Usuario;
import com.ltms.backend.usuario.UsuarioRegistroRequest;
import com.ltms.backend.usuario.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("conductor")
@RequiredArgsConstructor
public class ConductorController {
    private final ConductorService conductorService;
    private final ConductorMapper conductorMapper;

    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConductorDTO> crearConductor(
            @Valid @ModelAttribute ConductorDTO request,
            @RequestPart("profileImage") MultipartFile profileImage,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(conductorService.crearConductor(request, profileImage, connectedUser));
    }

    @GetMapping("/showAllConductores")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<ConductorDTO>> findAllConductores(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size,
            @RequestParam(name = "sort", defaultValue = "createdDate", required = false) String sort,
            @RequestParam(name = "order", defaultValue = "desc", required = false) String order
    ){
        return ResponseEntity.ok(conductorService.findAllConductores(page, size, sort, order));
    }

    @GetMapping("/getConductor/{idConductor}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConductorDTO> getConductor(
        @PathVariable Integer idConductor
    ){
        Conductor conductor = conductorService.getConductor(idConductor);
        ConductorDTO conductorDTO = conductorMapper.toConductorDto(conductor);
        return ResponseEntity.ok(conductorDTO);
    }

    /**
     * Actualizar la imagen de perfil del conductor.
     */
    @PutMapping("/actualizar/{id}/imagen")
    public ResponseEntity<Conductor> actualizarImagenPerfil(
            @PathVariable Integer id,
            @RequestParam("profileImage") MultipartFile imagen) throws IOException {
        Conductor actualizado = conductorService.actualizarImagenPerfil(id, imagen);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Actualizar la información del perfil del conductor segun su id.
     */
    @PutMapping("/actualizar/{idConductor}")
    public ResponseEntity<Conductor> actualizarDatosConductor(
            @PathVariable Integer idConductor,
            @RequestBody ConductorDTO request) {
        Conductor conductorActualizado = conductorService.actualizarDatosConductor(idConductor, request);
        return ResponseEntity.ok(conductorActualizado);
    }

    /**
     * Deshabilitar al conductor.
     */
    @DeleteMapping("/disable/{id}")
    public ResponseEntity<String> disableConductor(@PathVariable Integer id) {
        conductorService.disableConductor(id);
        return ResponseEntity.ok("Conductor inactivado exitosamente.");
    }

    @GetMapping("/mostrar-basic")
    public List<ConductorListaDTO> mostrarConductoresBasic() {
        return conductorService.obtenerTodosLosConductores();
    }
}
