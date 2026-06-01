package com.example.demo.service.material;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.MaterialDto;
import com.example.demo.model.Municipalidad;
import com.example.demo.model.Material;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.repository.MunicipalidadRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final MunicipalidadRepository municipalidadRepository;

    @Override
    @Transactional
    public Material createMaterial(MaterialDto materialDto, Long municipalidadId) {
        if (materialDto.getNombre_material() == null || materialDto.getNombre_material().isBlank()) {
            throw new RuntimeException("El nombre del material es obligatorio");
        }
        if (materialDto.getStock() == null) {
            throw new RuntimeException("El stock del material es obligatorio");
        }
        if (municipalidadId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La municipalidad del usuario es obligatoria");
        }

        Municipalidad municipalidad = municipalidadRepository.findById(municipalidadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipalidad no encontrada con id: " + municipalidadId));

        Material material = new Material();
        material.setCodigo_material(generarCodigoMaterial());
        material.setNombre_item(materialDto.getNombre_material());
        material.setStock(materialDto.getStock());
        material.setMunicipalidad(municipalidad);
        return materialRepository.save(material);
    }

    @Override
    public MaterialDto getMaterialById(Long id, Long municipalidadId) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material no encontrado con id: " + id));
        validarMunicipalidad(material, municipalidadId);
        return new MaterialDto(material.getMaterial_id(), material.getCodigo_material(), material.getNombre_item(), material.getStock(),
            material.getMunicipalidad() != null ? material.getMunicipalidad().getMunicipalidad_id() : null);
    }

    @Override
    public Material updateMaterial(Long id, MaterialDto materialDto, Long municipalidadId) {
        return materialRepository.findById(id)
                .map(existingMaterial -> {
                    validarMunicipalidad(existingMaterial, municipalidadId);

                    if (materialDto.getNombre_material() != null && !materialDto.getNombre_material().isBlank()) {
                        existingMaterial.setNombre_item(materialDto.getNombre_material());
                    }
                    if (materialDto.getStock() != null) {
                        existingMaterial.setStock(materialDto.getStock());
                    }
                    return materialRepository.save(existingMaterial);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material no encontrado con id: " + id));
    
    }

    @Override
    public void deleteMaterial(Long id, Long municipalidadId) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material no encontrado con id: " + id));
        validarMunicipalidad(material, municipalidadId);
        materialRepository.deleteById(id);
    }
    

    @Override
    public List<MaterialDto> getAllMaterials(Long municipalidadId) {
        if (municipalidadId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La municipalidad del usuario es obligatoria");
        }
        return materialRepository.findAllByMunicipalidadId(municipalidadId).stream()
            .map(material -> new MaterialDto(material.getMaterial_id(), material.getCodigo_material(), material.getNombre_item(), material.getStock(),
                material.getMunicipalidad() != null ? material.getMunicipalidad().getMunicipalidad_id() : null))
                .collect(java.util.stream.Collectors.toList());
    }

    private String generarCodigoMaterial() {
        String ultimoCodigo = materialRepository.findTopCodigoMaterial().orElse(null);
        if (ultimoCodigo == null || ultimoCodigo.isBlank()) {
            return "MAT-001";
        }

        String numeroTexto = ultimoCodigo.replaceFirst("^MAT-", "");
        int siguienteNumero = Integer.parseInt(numeroTexto) + 1;
        return String.format("MAT-%03d", siguienteNumero);
    }

    private void validarMunicipalidad(Material material, Long municipalidadId) {
        Long materialMunicipalidadId = material.getMunicipalidad() != null ? material.getMunicipalidad().getMunicipalidad_id() : null;
        if (municipalidadId == null || materialMunicipalidadId == null || !materialMunicipalidadId.equals(municipalidadId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a este material");
        }
    }
    
}
