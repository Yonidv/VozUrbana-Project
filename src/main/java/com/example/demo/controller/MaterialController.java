package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.MaterialDto;
import com.example.demo.service.material.MaterialService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/material")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;

    @PostMapping("/create")
    public ResponseEntity<?> createMaterial(@Valid @RequestBody MaterialDto material) {
        HashMap<String, Object> response = new HashMap<>();
        var newMaterial = materialService.createMaterial(material);
        if (newMaterial == null) {
            response.put("mensaje", "Error al crear el material");
        } else {
            response.put("mensaje", "Material creado exitosamente");
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMaterial(@PathVariable Long id, @Valid @RequestBody MaterialDto materialDto) {
        HashMap<String, Object> response = new HashMap<>();
        var materialUpdate = materialService.updateMaterial(id, materialDto);
        if (materialUpdate == null) {
            response.put("mensaje", "Error al actualizar el material");
        } else {
            response.put("mensaje", "Material actualizado exitosamente");
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long id) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            materialService.deleteMaterial(id);
            response.put("mensaje", "Material eliminado exitosamente");
        } catch (Exception e) {
            response.put("mensaje", "Error al eliminar el material: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MaterialDto>> seeAllMaterials() {
        return ResponseEntity.ok(materialService.getAllMaterials());
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<MaterialDto> searchMaterialById(@PathVariable Long id) {
        MaterialDto material = materialService.getMaterialById(id);
        return ResponseEntity.ok(material);
    }
}
