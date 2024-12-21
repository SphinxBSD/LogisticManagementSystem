package com.ltms.backend.documento;

import com.ltms.backend.file.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("documento")
@RequiredArgsConstructor
public class DocumentoController {
    private final DocumentoService documentoService;
    private final FileStorageService fileStorageService;

    @PostMapping("/create")
    public ResponseEntity<Documento> createDocumento(
            @Valid @ModelAttribute DocumentoDTO documentoDTO,
            @RequestPart("file") MultipartFile file) {
        Documento savedDocumento = documentoService.createDocumento(file, documentoDTO);
        return ResponseEntity.ok(savedDocumento);
    }

    /**
     * Endpoint para obtener un documento por su ID.
     */
    @GetMapping("/getDocumento/{id}")
    public ResponseEntity<DocumentoDTO> getDocumentoById(@PathVariable Integer id) {
        DocumentoDTO documentoDTO = documentoService.getDocumentoById(id);
        return ResponseEntity.ok(documentoDTO);
    }

    /**
     * Endpoint para obtener todos los documentos asociados a una entidad.
     */
    @GetMapping("/entidad/{idEntidad}")
    public ResponseEntity<List<DocumentoDTO>> getDocumentosByEntidad(
            @PathVariable Integer idEntidad,
            @RequestParam("tipoEntidad") DocumentoEntidad.TipoEntidad tipoEntidad) {

        List<DocumentoDTO> documentos = documentoService.getDocumentosByEntidad(idEntidad, tipoEntidad);
        return ResponseEntity.ok(documentos);
    }

    /**
     * Endpoint para actualizar un documento existente.
     */
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoDTO> updateDocumento(
            @PathVariable Integer id,
            @Valid @ModelAttribute DocumentoDTO documentoDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        DocumentoDTO updatedDocumento = documentoService.updateDocumento(id, file, documentoDTO);

        return ResponseEntity.ok(updatedDocumento);
    }

    /**
     * Endpoint para eliminar un documento.
     */
    @DeleteMapping("/delete/{idDocumento}")
    public ResponseEntity<Void> deleteDocumento(@PathVariable Integer idDocumento) {
        documentoService.deleteDocumento(idDocumento);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para descargar un archivo.
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
        DocumentoDTO documentoDTO = documentoService.getDocumentoById(id);

        // Cargar el archivo como recurso
        Resource resource = documentoService.loadFileAsResource(documentoDTO.getRutaArchivo());

        // Determinar el tipo de contenido del archivo
        String contentType = "application/octet-stream";

        // Devolver el archivo
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<DocumentoTipo>> getAllDocumentoTipo(){
        return ResponseEntity.ok(documentoService.getAllDocumentoTipo());
    }
}
