package com.ltms.backend.auth;

import com.ltms.backend.email.EmailService;
import com.ltms.backend.email.EmailTemplateName;
import com.ltms.backend.persona.Persona;
import com.ltms.backend.persona.PersonaRepository;
import com.ltms.backend.rol.RolRepository;
import com.ltms.backend.security.JwtService;
import com.ltms.backend.usuario.Usuario;
import com.ltms.backend.usuario.UsuarioRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void registrar(RegistrationRequest request) throws MessagingException{
        var usarioRole = rolRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalStateException("El rol USER no fue creado"));
        var persona = Persona.builder()
                .nombres(request.getNombres())
                .paterno(request.getPaterno())
                .materno(request.getMaterno())
                .ci(request.getCi())
                .fechaNac(request.getFechaNac())
                .direccion(request.getDireccion())
                .celular(request.getCelular())
                .estado("activo")
                .build();

        var usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(List.of(usarioRole))
                .build();

        usuario.setPersona(persona);
        persona.setUsuario(usuario);

        personaRepository.save(persona);
//        usuarioRepository.save(usuario);
        sendValidationEmail(usuario, request.getPassword());
    }

    private void sendValidationEmail(Usuario usuario, String code)
            throws MessagingException{
        emailService.sendEmail(
                usuario.getEmail(),
                usuario.getUsername(),
                EmailTemplateName.NOTICE_ACCOUNT,
                activationUrl,
                code,
                "Credenciales de cuenta"
        );
    }

    public AuthenticationResponse autenticar(AuthenticationRequest request){
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((Usuario)auth.getPrincipal());
        claims.put("username: ", user.getUsername());
        claims.put("id_user:",user.getIdUsuario());
        var jwtToken = jwtService.generateToken(claims, (Usuario) auth.getPrincipal());
//        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
