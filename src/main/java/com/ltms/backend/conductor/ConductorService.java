package com.ltms.backend.conductor;

import com.ltms.backend.common.PageResponse;
import com.ltms.backend.file.FileStorageService;
import com.ltms.backend.persona.Persona;
import com.ltms.backend.persona.PersonaRepository;
import com.ltms.backend.usuario.Usuario;
import com.ltms.backend.usuario.UsuarioRegistroRequest;
import com.ltms.backend.usuario.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConductorService {
    private final ConductorRepository conductorRepository;
    private final ConductorMapper conductorMapper;
    private final PersonaRepository personaRepository;
    private final FileStorageService fileStorageService;

    private final String uploadDir = "conductor/";

    public ConductorDTO crearConductor(
            ConductorDTO request,
            MultipartFile profileImage,
            Authentication connectedUser
    )  {

        Usuario user = ((Usuario) connectedUser.getPrincipal());
        // Guardar la imagen y obtener la URL
        String imageUrl = fileStorageService.saveFile(profileImage, uploadDir + request.getCi() + "_" + request.getLicencia());

        // Crear y guardar Persona
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

        // Crear y guardar conductor
        Conductor conductor = Conductor.builder()
                .persona(personaGuardada)
                .licencia(request.getLicencia())
                .descripcion(request.getDescripcion())
                .fechaContrato(request.getFechaContrato())
                .estado("disponible")
                .id(request.getIdConductor())
                .build();
        conductorRepository.save(conductor);

        return request;
    }

    public PageResponse<ConductorDTO> findAllConductores(
            int page,
            int size,
            String sort,
            String order
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Conductor> conductores = conductorRepository.findAllConductores(pageable);
        List<ConductorDTO> conductorDTOList = conductores.stream()
                .map(conductorMapper::toConductorDto)
                .toList();
        return new PageResponse<>(
                conductorDTOList,
                conductores.getNumber(),
                conductores.getSize(),
                conductores.getTotalElements(),
                conductores.getTotalPages(),
                conductores.isFirst(),
                conductores.isLast());
    }

    @Transactional(readOnly = true)
    public Conductor getConductor(Integer idConductor) {
        return conductorRepository.findById(idConductor)
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado con ID: " + idConductor));
    }

    @Transactional
    public Conductor actualizarImagenPerfil(Integer idConductor, MultipartFile nuevaImagen) throws IOException {
        // Buscar el usuario por ID
        Conductor conductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        // Guardar la nueva imagen y obtener la URL
        String nuevaImageUrl = fileStorageService.saveFile(nuevaImagen, uploadDir + conductor.getPersona().getCi() + "_" + conductor.getLicencia());

        // Actualizar la URL de la imagen en la entidad Persona
        Persona persona = conductor.getPersona();
        persona.setProfile(nuevaImageUrl);

        // Guardar los cambios en la persona
        personaRepository.save(persona);

        return conductor;
    }

    public Conductor actualizarDatosConductor(Integer idConductor, ConductorDTO request) {
        Conductor currentConductor = conductorRepository.findById(idConductor)
                .orElseThrow(() -> new IllegalStateException("Conductor no encontrado"));
        Persona persona = currentConductor.getPersona();
        if (persona == null) {
            throw new IllegalStateException("Persona: "+ currentConductor.getId());
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

        currentConductor.setDescripcion(request.getDescripcion());
        currentConductor.setLicencia(request.getLicencia());
        currentConductor.setFechaContrato(request.getFechaContrato());
        return conductorRepository.save(currentConductor);
    }

    @Transactional
    public void disableConductor(Integer conductorId) {
        Conductor conductor = conductorRepository.findById(conductorId)
                .orElseThrow(() -> new IllegalStateException("Conductor no encontrado"));

        Persona persona = conductor.getPersona();
        persona.setEstado("inactivo");

        personaRepository.save(persona);
    }

    @Transactional
    public List<ConductorListaDTO> obtenerTodosLosConductores() {
        return conductorRepository.findAllConductorDTO();
    }

}
