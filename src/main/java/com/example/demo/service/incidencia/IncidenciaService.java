package com.example.demo.service.incidencia;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.IncidenciaDto;
import com.example.demo.model.Incidencia;

public interface IncidenciaService {
    Incidencia createIncidencia(IncidenciaDto incidencia,MultipartFile file) throws IOException;
    IncidenciaDto getIncidenciaById(Long id);
    IncidenciaDto updateIncidencia(Long id, IncidenciaDto incidencia);
    void deleteIncidencia(Long id);
    List<IncidenciaDto> getAllIncidencias();
    
}
