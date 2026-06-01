package com.example.demo.service.material;

import java.util.List;

import com.example.demo.dtos.MaterialDto;
import com.example.demo.model.Material;

public interface MaterialService {
    Material createMaterial(MaterialDto materialDto, Long municipalidadId);
    MaterialDto getMaterialById(Long id, Long municipalidadId);
    Material updateMaterial(Long id, MaterialDto materialDto, Long municipalidadId);
    void deleteMaterial(Long id, Long municipalidadId);
    List<MaterialDto> getAllMaterials(Long municipalidadId);
}
