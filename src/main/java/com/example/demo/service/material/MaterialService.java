package com.example.demo.service.material;

import java.util.List;

import com.example.demo.dtos.MaterialDto;
import com.example.demo.model.Material;

public interface MaterialService {
    Material createMaterial(MaterialDto materialDto);
    MaterialDto getMaterialById(Long id);
    Material updateMaterial(Long id, MaterialDto materialDto);
    void deleteMaterial(Long id);
    List<MaterialDto> getAllMaterials();
}
