package com.example.demo.service.incidencia;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.IncidenciaDto;
import com.example.demo.model.Incidencia;

public interface IncidenciaService {
    Incidencia createIncidencia(IncidenciaDto incidencia, MultipartFile file, Long municipalidadId) throws IOException;
    IncidenciaDto getIncidenciaById(Long id, Long municipalidadId);
    IncidenciaDto updateIncidencia(Long id, IncidenciaDto incidencia, Long municipalidadId);
    void deleteIncidencia(Long id, Long municipalidadId);
    List<IncidenciaDto> getAllIncidencias(Long municipalidadId);
    
}
