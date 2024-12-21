package com.ltms.backend.usuario;

import com.ltms.backend.auth.RegistrationRequest;
import com.ltms.backend.common.PageResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ADMIN')") // Ajusta según tus necesidades de roles
    // Is important to consider that using "hasRole" is means that is looking for the rol-name "ROLE_USER"
    // Alternatively use annotation like this @PreAuthorize("hasAuthotity('READ')")and drop the roles.
    public ResponseEntity<UsuarioRegistroResponse> registrarUsuario(
            @Valid @ModelAttribute UsuarioRegistroRequest request, //I am using ModelAttribute because i can not send a json for the DTO using RequestBody
            @RequestPart("profileImage") MultipartFile profileImage,
            Authentication connectedUser) {
        try {
            Usuario usuario = usuarioService.registrarUsuario(request, profileImage);
            UsuarioRegistroResponse response = new UsuarioRegistroResponse("Usuario registrado exitosamente", usuario.getIdUsuario());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            UsuarioRegistroResponse response = new UsuarioRegistroResponse(ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            UsuarioRegistroResponse response = new UsuarioRegistroResponse(e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/showAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UsuarioResponse>> findAllUsers(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size
    ){
        return ResponseEntity.ok(usuarioService.findAllUsers(page, size));
    }

    /**
     * Deshabilitar un usuario.
     * Solo accesible para ROLE_ADMIN.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/disable")
    public ResponseEntity<?> disableUsuario(@PathVariable Integer id) {
        usuarioService.disableUsuario(id);
        return ResponseEntity.ok().body("Usuario deshabilitado exitosamente.");
    }

    /**
     * Eliminar un usuario.
     * Solo accesible para ROLE_ADMIN.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok().body("Usuario eliminado exitosamente.");
    }

    /**
     * Obtener el perfil del usuario autenticado.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getProfile")
    public ResponseEntity<UsuarioResponse> getProfile(
            Authentication connectedUser
    ) {
        UsuarioResponse profile = usuarioService.getProfile(connectedUser);
        return ResponseEntity.ok(profile);
    }

    /**
     * Actualizar la información del perfil del usuario segun su id.
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Usuario> actualizarDatosUsuario(
            @PathVariable Integer id,
            @RequestBody UsuarioRegistroRequest request) {
        Usuario usuarioActualizado = usuarioService.actualizarDatosUsuario(id, request);
        return ResponseEntity.ok(usuarioActualizado);
    }

    /**
     * Actualizar la imagen de perfil del usuario.
     */
    @PutMapping("/actualizar/{id}/imagen")
    public ResponseEntity<Usuario> actualizarImagenPerfil(
            @PathVariable Integer id,
            @RequestParam("profileImage") MultipartFile imagen) throws IOException {
        Usuario actualizado = usuarioService.actualizarImagenPerfil(id, imagen);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Actualizar la contraseña del usuario autenticado.
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/actualizar/password")
    public ResponseEntity<?> updatePassword(
            @RequestBody @Valid UpdatePasswordDTO updatePasswordDTO,
            Authentication connectedUser
            ) {
        usuarioService.updatePassword(updatePasswordDTO, connectedUser);
        // Crear un objeto de respuesta JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password actualizada exitosamente.");
        return ResponseEntity.ok(response);
    }




}
