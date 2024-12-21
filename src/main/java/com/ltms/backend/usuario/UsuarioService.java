package com.ltms.backend.usuario;

import com.ltms.backend.common.PageResponse;
import com.ltms.backend.email.EmailService;
import com.ltms.backend.email.EmailTemplateName;
import com.ltms.backend.file.FileStorageService;
import com.ltms.backend.persona.Persona;
import com.ltms.backend.persona.PersonaRepository;
import com.ltms.backend.rol.RolRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.*;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final FileStorageService fileStorageService;
    private final UsuarioMapper usuarioMapper;
    private final EmailService emailService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    // Directorio donde se almacenarán las imágenes
    private final String uploadDir = "personal/";

    @Transactional
    public Usuario registrarUsuario(
            UsuarioRegistroRequest request,
            MultipartFile profileImage
            ) throws IOException {

        String passTemp = usuarioMapper.generarPassword();
        var usarioRole = rolRepository
                .findByName(request.getRol())
                .orElseThrow(() -> new IllegalStateException("El rol " + request.getRol() + " no fue creado"));
        // Validar que el username y email no existan
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        // Guardar la imagen y obtener la URL
        String imageUrl = fileStorageService.saveFile(profileImage, uploadDir + request.getUsername());
//        user.setProfileImageUrl(imageUrl);

        //crear y guardar Persona
        var persona = Persona.builder()
                .nombres(request.getNombres())
                .paterno(request.getPaterno())
                .materno(request.getMaterno())
                .ci(request.getCi())
                .fechaNac(request.getFechaNac())
                .direccion(request.getDireccion())
                .celular(request.getCelular())
                .profile(imageUrl)
                .estado("activo")
                .build();
        Persona personaGuardada = personaRepository.save(persona);

        var usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(passTemp))
                .enabled(true)
                .roles(List.of(usarioRole))
                .persona(personaGuardada)
                .build();
        try {
            sendValidationEmail(usuario, passTemp);
        } catch (Exception e){
            log.info("Error al enviar el correo.", e);
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void disableUsuario(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalStateException("El usuario: " + usuarioId + " no fue creado"));

        // Deshabilitar el usuario
//        usuario.setEnabled(true);
//        usuarioRepository.save(usuario);

        // Actualizar el estado de la persona a "inactivo"
        Persona persona = usuario.getPersona(); // Asumiendo que Usuario tiene una relación con Persona
        if (persona != null) {
            persona.setEstado("inactivo");
            personaRepository.save(persona);
        }
    }

    @Transactional
    public void deleteUsuario(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalStateException("El usuario: " + usuarioId + " no fue creado"));

        // Eliminar el usuario (en este caso, solo deshabilitar)
        usuario.setEnabled(false);
        usuarioRepository.save(usuario);

        // Opcional: Puedes implementar una lógica adicional para eliminar datos relacionados si es necesario
    }


    public PageResponse<UsuarioResponse> findAllUsers(
            int page,
            int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Usuario> usuarios = usuarioRepository.findAllUsers(pageable);
        List<UsuarioResponse> usuarioResponse = usuarios.stream()
                .map(usuarioMapper::toUsuarioResponse)
                .toList();
        return new PageResponse<>(
                usuarioResponse,
                usuarios.getNumber(),
                usuarios.getSize(),
                usuarios.getTotalElements(),
                usuarios.getTotalPages(),
                usuarios.isFirst(),
                usuarios.isLast());
    }

    @Transactional(readOnly = true)
    public UsuarioResponse getProfile(Authentication connectedUser) {
        Usuario currentUsuario = ((Usuario) connectedUser.getPrincipal());
        Persona persona = currentUsuario.getPersona();
        if (persona == null) {
            throw new IllegalStateException("Persona: "+ currentUsuario.getIdUsuario());
        }
//        UsuarioResponse usuarioResponse = usuarioMapper.toUsuarioResponse(currentUsuario);
        return usuarioMapper.toUsuarioResponse(currentUsuario);
    }

    @Transactional
    public Usuario actualizarDatosUsuario(
        Integer idUsuario,
            UsuarioRegistroRequest request
    ){
        Usuario currentUsuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        Persona persona = currentUsuario.getPersona();
        if (persona == null) {
            throw new IllegalStateException("Persona: "+ currentUsuario.getIdUsuario());
        }
        persona.setNombres(request.getNombres());
        persona.setPaterno(request.getPaterno());
        persona.setMaterno(request.getMaterno());
        persona.setCi(request.getCi());
        persona.setFechaNac(request.getFechaNac());
        persona.setDireccion(request.getDireccion());
        persona.setCelular(request.getCelular());

        // Guardar los cambios en la persona
        personaRepository.save(persona);

        // Actualizar otros datos del usuario si es necesario
        currentUsuario.setEmail(request.getEmail());
        return usuarioRepository.save(currentUsuario);

    }

    @Transactional
    public Usuario actualizarImagenPerfil(Integer usuarioId, MultipartFile nuevaImagen) throws IOException {
        // Buscar el usuario por ID
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        // Guardar la nueva imagen y obtener la URL
        String nuevaImageUrl = fileStorageService.saveFile(nuevaImagen, uploadDir + usuario.getUsername());

        // Actualizar la URL de la imagen en la entidad Persona
        Persona persona = usuario.getPersona();
        persona.setProfile(nuevaImageUrl);

        // Guardar los cambios en la persona
        personaRepository.save(persona);

        return usuario;
    }

    private void sendValidationEmail(Usuario usuario, String code)
            throws MessagingException {
        emailService.sendEmail(
                usuario.getEmail(),
                usuario.getUsername(),
                EmailTemplateName.NOTICE_ACCOUNT,
                activationUrl,
                code,
                "Credenciales de cuenta"
        );
    }

    @Transactional
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO, Authentication connectedUser) {
        Usuario currentUser = ((Usuario) connectedUser.getPrincipal());

        // Verificar que la contraseña actual es correcta
        if (!passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        // Validar que la nueva contraseña y la confirmación coincidan
//        if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmNewPassword())) {
//            throw new IllegalArgumentException("Las nuevas contraseñas no coinciden.");
//        }

        // Puedes añadir validaciones adicionales para la fortaleza de la contraseña

        // Actualizar la contraseña
        currentUser.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        usuarioRepository.save(currentUser);
    }


}
