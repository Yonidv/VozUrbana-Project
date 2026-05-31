package com.example.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.IncidenciaDto;
import com.example.demo.model.Incidencia;
import com.example.demo.service.incidencia.IncidenciaService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/incidencia")
@RequiredArgsConstructor
public class IncidenciaController {


    private final IncidenciaService incidenciaService;
    private final ObjectMapper objectMapper;

 

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createIncidencia(
            @RequestPart("incidencia") String incidenciaJson,
            @RequestPart("image") MultipartFile imageFile) {

        HashMap<String, Object> response = new HashMap<>();
        try {
            IncidenciaDto incidenciaDto = objectMapper.readValue(incidenciaJson, IncidenciaDto.class);
            Incidencia newIncidencia = incidenciaService.createIncidencia(incidenciaDto, imageFile);
            response.put("mensaje", "Incidencia creado exitosamente");
            response.put("incidencia_id", newIncidencia.getIncidencia_id());
            response.put("foto", newIncidencia.getUrl_foto());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            response.put("mensaje", "Error al procesar la imagen o el JSON de incidencia: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (RuntimeException e) {
            response.put("mensaje", "Error al crear la incidencia: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateIncidencia(@PathVariable Long id, @RequestBody IncidenciaDto incidencia) {
        HashMap<String, Object> response = new HashMap<>();
        var incidenciaUpdate = incidenciaService.updateIncidencia(id, incidencia);
        if (incidenciaUpdate == null) {
            response.put("mensaje", "Error al actualizar el incidencia");
        } else {
            response.put("mensaje", "Incidencia actualizado exitosamente");
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteIncidencia(@PathVariable Long id) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            incidenciaService.deleteIncidencia(id);
            response.put("mensaje", "Incidencia eliminado exitosamente");
        } catch (Exception e) {
            response.put("mensaje", "Error al eliminar el incidencia: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<IncidenciaDto>> seeAllIncidencias() {
        return ResponseEntity.ok(incidenciaService.getAllIncidencias());
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<IncidenciaDto> searchIncidenciaById(@PathVariable Long id) {
        return ResponseEntity.ok(incidenciaService.getIncidenciaById(id));
    }
}
