package com.ltms.backend.conductor;

import com.ltms.backend.file.FileUtils;
import com.ltms.backend.persona.Persona;
import org.springframework.stereotype.Service;

@Service
public class ConductorMapper {
    public ConductorDTO toConductorDto(Conductor conductor){
        return ConductorDTO.builder()
                .idConductor(conductor.getId())
                .fechaContrato(conductor.getFechaContrato())
                .licencia(conductor.getLicencia())
                .descripcion(conductor.getDescripcion())
                .estado(conductor.getEstado())
                .ci(conductor.getPersona().getCi())
                .idPersona(conductor.getPersona().getIdPersona())
                .nombres(conductor.getPersona().getNombres())
                .paterno(conductor.getPersona().getPaterno())
                .materno(conductor.getPersona().getMaterno())
                .fechaNac(conductor.getPersona().getFechaNac())
                .direccion(conductor.getPersona().getDireccion())
                .celular(conductor.getPersona().getCelular())
                .profile(FileUtils.readFileFromLocation(conductor.getPersona().getProfile()))
                .build();
    }

}
