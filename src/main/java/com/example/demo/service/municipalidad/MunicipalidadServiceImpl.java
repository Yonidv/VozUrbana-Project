package com.example.demo.service.municipalidad;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.MunicipalidadDto;

import com.example.demo.model.Municipalidad;
import com.example.demo.repository.MunicipalidadRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MunicipalidadServiceImpl implements MunicipalidadService {

    private final MunicipalidadRepository municipalidadRepository;

    @Override
    public Municipalidad save(MunicipalidadDto municipalidadDto) {
        Municipalidad municipalidad = new Municipalidad();
        municipalidad.setNombre_municipio(municipalidadDto.getNombre_municipio());
        return municipalidadRepository.save(municipalidad);
    }
    
    @Override
    public MunicipalidadDto findById(Long id) {
        Municipalidad municipalidad = municipalidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Municipalidad no encontrada con id: " + id));
        return new MunicipalidadDto(
                municipalidad.getMunicipalidad_id(),
                municipalidad.getNombre_municipio());
    }

    @Override
    public Municipalidad update(Long id, MunicipalidadDto municipalidadDto) {
        return municipalidadRepository.findById(id)
                .map(existingMunicipalidad -> {
                    existingMunicipalidad.setNombre_municipio(municipalidadDto.getNombre_municipio());
                    return municipalidadRepository.save(existingMunicipalidad);
                })
                .orElseThrow(() -> new RuntimeException("Municipalidad no encontrada con id: " + id));
    }

    @Override
    public void delete(Long id) {
        municipalidadRepository.deleteById(id);
    }

    @Override
    public List<MunicipalidadDto> findAll() {
        return municipalidadRepository.findAll().stream()
                .map(municipalidad -> new MunicipalidadDto(
                        municipalidad.getMunicipalidad_id(),
                        municipalidad.getNombre_municipio()))
                .collect(Collectors.toList());
    }
}
