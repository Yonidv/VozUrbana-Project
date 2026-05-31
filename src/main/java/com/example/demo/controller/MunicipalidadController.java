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

import com.example.demo.dtos.MunicipalidadDto;
import com.example.demo.service.municipalidad.MunicipalidadService;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/municipalidad")
@RequiredArgsConstructor
public class MunicipalidadController {
    private final MunicipalidadService municipalidadService;

    @PostMapping("/create")
    public ResponseEntity<?> createMunicipalidad(@RequestBody MunicipalidadDto municipalidad) {
        HashMap<String, Object> response = new HashMap<>();
        var newMunicipalidad = municipalidadService.save(municipalidad);
        if (newMunicipalidad == null) {
            response.put("mensaje", "Error al crear el municipalidad");
        } else {
            response.put("mensaje", "Municipalidad creado exitosamente");
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMunicipalidad(@PathVariable Long id, @RequestBody MunicipalidadDto municipalidad) {
        HashMap<String, Object> response = new HashMap<>();
        var municipalidadUpdate = municipalidadService.update(id, municipalidad);
        if (municipalidadUpdate == null) {
            response.put("mensaje", "Error al actualizar el municipalidad");
        } else {
            response.put("mensaje", "Municipalidad actualizado exitosamente");
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMunicipalidad(@PathVariable Long id) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            municipalidadService.delete(id);
            response.put("mensaje", "Municipalidad eliminado exitosamente");
        } catch (Exception e) {
            response.put("mensaje", "Error al eliminar el municipalidad: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MunicipalidadDto>> seeAllMunicipalidads() {
        return ResponseEntity.ok(municipalidadService.findAll());
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<MunicipalidadDto> searchMunicipalidadById(@PathVariable Long id) {
        MunicipalidadDto municipalidad = municipalidadService.findById(id);
        return ResponseEntity.ok(municipalidad);
    }
}
