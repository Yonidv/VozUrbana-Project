package com.example.demo.service.material;

import java.util.List;

import com.example.demo.model.Material;

public interface MaterialService {
    Material createMaterial(Material material);
    Material getMaterialById(Long id);
    Material updateMaterial(Long id, Material material);
    void deleteMaterial(Long id);
    List<Material> getAllMaterials();
}
