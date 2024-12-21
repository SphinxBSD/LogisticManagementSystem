package com.ltms.backend.usuario;

import com.ltms.backend.file.FileUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UsuarioMapper {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();
    public String generarPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    public UsuarioResponse toUsuarioResponse(Usuario usuario){
        return UsuarioResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .email(usuario.getEmail())
                .username(usuario.getUsername())
                .nombres(usuario.getPersona().getNombres())
                .paterno(usuario.getPersona().getPaterno())
                .materno(usuario.getPersona().getMaterno())
                .ci(usuario.getPersona().getCi())
                .fechaNac(usuario.getPersona().getFechaNac())
                .celular(usuario.getPersona().getCelular())
                .estado(usuario.getPersona().getEstado())
                .rol(usuario.getRoles().get(0).getName())
                .enabled(usuario.isEnabled())
                .direccion(usuario.getPersona().getDireccion())
                .profileImage(FileUtils.readFileFromLocation(usuario.getPersona().getProfile()))
                .build();
    }
}
