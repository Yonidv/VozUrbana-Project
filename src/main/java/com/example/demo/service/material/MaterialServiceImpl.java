package com.example.demo.service.material;

import java.util.List;

import org.springframework.stereotype.Service;

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
    public Material createMaterial(MaterialDto materialDto) {
        if (materialDto.getNombre_material() == null || materialDto.getNombre_material().isBlank()) {
            throw new RuntimeException("El nombre del material es obligatorio");
        }
        if (materialDto.getStock() == null) {
            throw new RuntimeException("El stock del material es obligatorio");
        }
        if (materialDto.getMunicipalidad_id() == null) {
            throw new RuntimeException("La municipalidad es obligatoria");
        }

        Municipalidad municipalidad = municipalidadRepository.findById(materialDto.getMunicipalidad_id())
                .orElseThrow(() -> new RuntimeException("Municipalidad no encontrada con id: " + materialDto.getMunicipalidad_id()));

        Material material = new Material();
        material.setNombre_item(materialDto.getNombre_material());
        material.setStock(materialDto.getStock());
        material.setMunicipalidad(municipalidad);
        return materialRepository.save(material);
    }

    @Override
    public MaterialDto getMaterialById(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material no encontrado con id: " + id));
        return new MaterialDto(material.getMaterial_id(), material.getNombre_item(), material.getStock(),
            material.getMunicipalidad() != null ? material.getMunicipalidad().getMunicipalidad_id() : null);
    }

    @Override
    public Material updateMaterial(Long id, MaterialDto materialDto) {
        return materialRepository.findById(id)
                .map(existingMaterial -> {
                    if (materialDto.getNombre_material() != null && !materialDto.getNombre_material().isBlank()) {
                        existingMaterial.setNombre_item(materialDto.getNombre_material());
                    }
                    if (materialDto.getStock() != null) {
                        existingMaterial.setStock(materialDto.getStock());
                    }
                    if (materialDto.getMunicipalidad_id() != null) {
                        Municipalidad municipalidad = municipalidadRepository.findById(materialDto.getMunicipalidad_id())
                                .orElseThrow(() -> new RuntimeException("Municipalidad no encontrada con id: " + materialDto.getMunicipalidad_id()));
                        existingMaterial.setMunicipalidad(municipalidad);
                    }
                    return materialRepository.save(existingMaterial);
                })
                .orElseThrow(() -> new RuntimeException("Material no encontrado con id: " + id));
    
    }

    @Override
    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }
    

    @Override
    public List<MaterialDto> getAllMaterials() {
        return materialRepository.findAll().stream()
            .map(material -> new MaterialDto(material.getMaterial_id(), material.getNombre_item(), material.getStock(),
                material.getMunicipalidad() != null ? material.getMunicipalidad().getMunicipalidad_id() : null))
                .collect(java.util.stream.Collectors.toList());
    }
    
}
