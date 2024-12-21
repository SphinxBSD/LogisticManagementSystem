package com.ltms.backend.documento;

import com.ltms.backend.conductor.Conductor;
import com.ltms.backend.conductor.ConductorRepository;
import com.ltms.backend.file.FileStorageService;
import com.ltms.backend.vehiculo.Vehiculo;
import com.ltms.backend.vehiculo.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentoService {
    private final DocumentoRepository documentoRepository;
    private final DocumentoTipoRepository documentoTipoRepository;
    private final DocumentoEntidadRepository documentoEntidadRepository;
    private final FileStorageService fileStorageService;
    private final ConductorRepository conductorRepository;
    private final VehiculoRepository vehiculoRepository;
    private final DocumentoMapper documentoMapper;

    private final String uploadDir = "documentos";

    @Transactional
    public Documento createDocumento(
            MultipartFile file,
            DocumentoDTO documentoDTO
    ) {
        // Validar el tipo de archivo
//        if (!isSupportedContentType(file.getContentType())) {
//            throw new FileStorageException("Tipo de archivo no soportado: " + file.getContentType());
//        }

        // Obtener el DocumentoTipo
        DocumentoTipo documentoTipo = documentoTipoRepository.findById(documentoDTO.getIdDocumentoTipo())
                .orElseThrow(() -> new IllegalStateException("Tipo de documento no encontrado"));

        // Crear instancia de Documento
        Documento documento = new Documento();
        documento.setDocumentoTipo(documentoTipo);
        documento.setFechaVencimiento(documentoDTO.getFechaVencimiento());
        documento.setEstadoDocumento(documentoDTO.getEstadoDocumento());

        // Generar el subpath basado en el tipo de entidad
        System.out.println("Tipo entidad");
        System.out.println(documentoDTO.getTipoEntidad());
        String subPath = getSubPath(documentoDTO.getIdEntidad(), documentoDTO.getTipoEntidad());

        // Almacenar el archivo
        String fileName = fileStorageService.saveFile(file, uploadDir + "/" + subPath);
        documento.setRutaArchivo(fileName);

        // Guardar el documento
        Documento savedDocumento = documentoRepository.save(documento);

        // Crear la relación con la entidad
        DocumentoEntidad documentoEntidad = new DocumentoEntidad();
        documentoEntidad.setDocumento(savedDocumento);
        documentoEntidad.setIdEntidad(documentoDTO.getIdEntidad());
        documentoEntidad.setTipoEntidad(documentoDTO.getTipoEntidad());

        documentoEntidadRepository.save(documentoEntidad);

        return savedDocumento;
    }

    /**
     * Obtiene un documento por su ID.
     */
    @Transactional
    public DocumentoDTO getDocumentoById(Integer id) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Documento no encontrado con id: " + id));

        return documentoMapper.documentoToDTO(documento);
    }

    /**
     * Obtiene todos los documentos asociados a una entidad (Conductor o Vehículo).
     */
    @Transactional
    public List<DocumentoDTO> getDocumentosByEntidad(Integer idEntidad, DocumentoEntidad.TipoEntidad tipoEntidad) {
        List<DocumentoEntidad> documentoEntidades = documentoEntidadRepository.findByIdEntidadAndTipoEntidad(idEntidad, tipoEntidad);

        return documentoEntidades.stream()
                .map(de -> documentoMapper.documentoToDTO(de.getDocumento()))
                .collect(Collectors.toList());
    }

    /**
     * Actualiza un documento existente.
     */
    @Transactional
    public DocumentoDTO updateDocumento(Integer id, MultipartFile file, DocumentoDTO documentoDTO) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Documento no encontrado con id: " + id));

        if (file != null) {
            // Validar el tipo de archivo
//            if (!isSupportedContentType(file.getContentType())) {
//                throw new FileStorageException("Tipo de archivo no soportado: " + file.getContentType());
//            }

            // Eliminar el archivo anterior si es necesario
            // Almacenar el nuevo archivo
            String subPath = getSubPath(documentoDTO.getIdEntidad(), documentoDTO.getTipoEntidad());
            String fileName = fileStorageService.saveFile(file, uploadDir + "/" + subPath);
            documento.setRutaArchivo(fileName);
        }

        // Actualizar otros campos
        DocumentoTipo documentoTipo = documentoTipoRepository.findById(documentoDTO.getIdDocumentoTipo())
                .orElseThrow(() -> new IllegalStateException("Tipo de documento no encontrado"));

        documento.setDocumentoTipo(documentoTipo);
        documento.setFechaVencimiento(documentoDTO.getFechaVencimiento());
        documento.setEstadoDocumento(documentoDTO.getEstadoDocumento());

        Documento updatedDocumento = documentoRepository.save(documento);

        return documentoMapper.documentoToDTO(updatedDocumento);
    }

    /**
     * Elimina un documento por su ID.
     */
    @Transactional
    public void deleteDocumento(Integer idDocumento) {

        Documento documento = documentoRepository.findById(idDocumento)
                .orElseThrow(() -> new IllegalStateException("Documento no encontrado con id: " + idDocumento));

        documentoEntidadRepository.deleteById(idDocumento);

        // Eliminar el archivo físico si es necesario
        documentoRepository.delete(documento);
    }

    @Transactional
    public List<DocumentoTipo> getAllDocumentoTipo(){
        return documentoTipoRepository.findAll();
    }

    public Resource loadFileAsResource(String fullFilePath) {
        try {
//            String fullFilePath =  relativeFilePath;
            Path filePath = Paths.get(fullFilePath).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new IllegalStateException("Archivo no encontrado: " + fullFilePath);
            }
        } catch (MalformedURLException ex) {
            throw new IllegalStateException("Archivo no encontrado: " + fullFilePath, ex);
        }
    }

    private String getSubPath(Integer idEntidad, DocumentoEntidad.TipoEntidad tipoEntidad) {
        if (tipoEntidad == DocumentoEntidad.TipoEntidad.CONDUCTOR) {
            Conductor conductor = conductorRepository.findById(idEntidad)
                    .orElseThrow(() -> new IllegalStateException("Conductor no encontrado con ID: " + idEntidad));
            return conductor.getPersona().getCi() + "_" + conductor.getLicencia();

        } else if (tipoEntidad == DocumentoEntidad.TipoEntidad.VEHICULO) {
            Vehiculo vehiculo = vehiculoRepository.findById(idEntidad)
                    .orElseThrow(() -> new IllegalStateException("Vehículo no encontrado con ID: " + idEntidad));
            return vehiculo.getPlaca();
        } else {
            throw new IllegalArgumentException("Tipo de entidad no soportado");
        }
    }





}
