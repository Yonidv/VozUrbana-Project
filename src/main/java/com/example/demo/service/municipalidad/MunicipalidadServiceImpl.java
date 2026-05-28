package com.example.demo.service.municipalidad;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Municipalidad;
import com.example.demo.repository.MunicipalidadRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MunicipalidadServiceImpl implements MunicipalidadService {

    private MunicipalidadRepository municipalidadRepository;

    @Override
    public Municipalidad findById(Long id) {
        return municipalidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Municipalidad no encontrada con id: " + id));
    }

    @Override
    public Municipalidad save(Municipalidad municipalidad) {
        return municipalidadRepository.save(municipalidad);
    }

    @Override
    public Municipalidad update(Long id, Municipalidad municipalidad) {
        return municipalidadRepository.findById(id)
                .map(existingMunicipalidad -> {
                    existingMunicipalidad.setNombre_municipio(municipalidad.getNombre_municipio());
                    existingMunicipalidad.setIncidencias(municipalidad.getIncidencias());
                    existingMunicipalidad.setMateriales(municipalidad.getMateriales());
                    existingMunicipalidad.setUsuarios(municipalidad.getUsuarios());
                    return municipalidadRepository.save(existingMunicipalidad);
                })
                .orElseThrow(() -> new RuntimeException("Municipalidad no encontrada con id: " + id));
    }

    @Override
    public void delete(Long id) {
        municipalidadRepository.deleteById(id);
    }

    @Override
    public List<Municipalidad> findAll() {
        return municipalidadRepository.findAll();
    }
    
}
