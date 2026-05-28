package com.example.demo.service.material;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Material;
import com.example.demo.repository.MaterialRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private MaterialRepository materialRepository;

    @Override
    public Material createMaterial(Material material) {
        return materialRepository.save(material);
    }

    @Override
    public Material getMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material no encontrado con id: " + id));
    }

    @Override
    public Material updateMaterial(Long id, Material material) {
        return materialRepository.findById(id)
                .map(existingMaterial -> {
                    existingMaterial.setNombre_item(material.getNombre_item());
                    existingMaterial.setMaterial_id(material.getMaterial_id());
                    existingMaterial.setStock(material.getStock());
                    return materialRepository.save(existingMaterial);
                })
                .orElseThrow(() -> new RuntimeException("Material no encontrado con id: " + id));
    
    }

    @Override
    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }
    

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }
    
}
