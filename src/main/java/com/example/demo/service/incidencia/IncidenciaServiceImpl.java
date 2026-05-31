package com.example.demo.service.incidencia;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.DetalleIncidenciaMaterialDto;
import com.example.demo.dtos.IncidenciaDto;
import com.example.demo.enums.EstadoIncidencia;
import com.example.demo.model.Incidencia;
import com.example.demo.model.DetalleIncidenciaMaterial;
import com.example.demo.model.Material;
import com.example.demo.repository.IncidenciaRepository;
import com.example.demo.repository.DetalleIncidenciaMaterialRepository;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.service.image.ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidenciaServiceImpl implements IncidenciaService {


    private final IncidenciaRepository incidenciaRepository;
    private final DetalleIncidenciaMaterialRepository detalleIncidenciaMaterialRepository;
    private final MaterialRepository materialRepository;
    private final ImageService imageService;

    @Value("${app.file.base.url}")
    private String baseImageUrl;

    @Override
    @Transactional
    public Incidencia createIncidencia(IncidenciaDto incidenciaDto,MultipartFile imageFile) throws IOException{
        String imageUrl = imageService.storeImage(imageFile);
        Incidencia incidencia = new Incidencia();
        incidencia.setDescripcion(incidenciaDto.getDescripcion());
        incidencia.setUrl_foto(imageUrl);
        incidencia.setEstado(EstadoIncidencia.PENDIENTE);
        Incidencia savedIncidencia = incidenciaRepository.save(incidencia);
        procesarDetalles(savedIncidencia, incidenciaDto.getDetalles());
        return savedIncidencia;
    }

    @Override
    public IncidenciaDto getIncidenciaById(Long id) {
        Incidencia incidencia = incidenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada con id: " + id));

        List<DetalleIncidenciaMaterialDto> detalles = detalleIncidenciaMaterialRepository.findByIncidencia(incidencia).stream()
            .map(this::toDto)
            .collect(Collectors.toList());

        String foto = incidencia.getUrl_foto();
        if (foto != null && !foto.isBlank()) {
            foto = baseImageUrl + foto;
        }

        return new IncidenciaDto(
                incidencia.getIncidencia_id(),
                incidencia.getDescripcion(),
                foto,
                incidencia.getEstado(),
            detalles
        );
    }

    @Override
    @Transactional
    public IncidenciaDto updateIncidencia(Long id, IncidenciaDto incidenciaDto) {
        return incidenciaRepository.findById(id)
                .map(existingIncidencia -> {
                    if (incidenciaDto.getDescripcion() != null && !incidenciaDto.getDescripcion().isBlank()) {
                        existingIncidencia.setDescripcion(incidenciaDto.getDescripcion());
                    }
                    if (incidenciaDto.getFoto() != null && !incidenciaDto.getFoto().isBlank()) {
                        existingIncidencia.setUrl_foto(incidenciaDto.getFoto());
                    }
                    
                    if (incidenciaDto.getEstado() == EstadoIncidencia.RESUELTO) {
                        existingIncidencia.setEstado(EstadoIncidencia.RESUELTO);
                    }

                    procesarDetalles(existingIncidencia, incidenciaDto.getDetalles());

                    incidenciaRepository.save(existingIncidencia);
                    return getIncidenciaById(id);
                })
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada con id: " + id));
    }

    @Override
    public void deleteIncidencia(Long id) {
        incidenciaRepository.deleteById(id);
    }

    @Override
    public List<IncidenciaDto> getAllIncidencias() {
        return incidenciaRepository.findAll().stream()
                .map(incidencia -> getIncidenciaById(incidencia.getIncidencia_id()))
                .collect(Collectors.toList());
    }

    private void procesarDetalles(Incidencia incidencia, List<DetalleIncidenciaMaterialDto> detallesDto) {
        if (detallesDto == null || detallesDto.isEmpty()) {
            return;
        }

        Map<Long, DetalleIncidenciaMaterial> detallesExistentesPorId = detalleIncidenciaMaterialRepository.findByIncidencia(incidencia).stream()
                .collect(Collectors.toMap(DetalleIncidenciaMaterial::getDetalle_id, detalle -> detalle));

        for (DetalleIncidenciaMaterialDto detalleDto : detallesDto) {
            if (detalleDto.getCantidad() == null || detalleDto.getCantidad() <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a cero");
            }

            Material material = null;
            if (detalleDto.getMaterial_id() != null) {
                material = materialRepository.findById(detalleDto.getMaterial_id())
                        .orElseThrow(() -> new RuntimeException("Material no encontrado con id: " + detalleDto.getMaterial_id()));
            }

            if (detalleDto.getDetalle_id() != null) {
                DetalleIncidenciaMaterial detalleExistente = detallesExistentesPorId.get(detalleDto.getDetalle_id());
                if (detalleExistente == null) {
                    throw new RuntimeException("Detalle no encontrado con id: " + detalleDto.getDetalle_id());
                }

                Material materialAnterior = detalleExistente.getMaterial();
                int cantidadAnterior = detalleExistente.getCantidad_usada();
                Material materialFinal = material != null ? material : materialAnterior;
                if (materialFinal == null) {
                    throw new RuntimeException("El material es obligatorio para el detalle");
                }

                int cantidadFinal = detalleDto.getCantidad();

                if (materialAnterior != null && materialAnterior.getMaterial_id().equals(materialFinal.getMaterial_id())) {
                    int stockDisponibleReal = materialFinal.getStock() + cantidadAnterior;
                    if (cantidadFinal > stockDisponibleReal) {
                        throw new RuntimeException("La cantidad solicitada excede el stock disponible");
                    }

                    materialFinal.setStock(stockDisponibleReal - cantidadFinal);
                    materialRepository.save(materialFinal);
                } else {
                    if (materialFinal.getStock() < cantidadFinal) {
                        throw new RuntimeException("La cantidad solicitada excede el stock disponible");
                    }

                    if (materialAnterior != null) {
                        materialAnterior.setStock(materialAnterior.getStock() + cantidadAnterior);
                        materialRepository.save(materialAnterior);
                    }

                    materialFinal.setStock(materialFinal.getStock() - cantidadFinal);
                    materialRepository.save(materialFinal);
                }

                detalleExistente.setIncidencia(incidencia);
                detalleExistente.setMaterial(materialFinal);
                detalleExistente.setCantidad_usada(cantidadFinal);
                detalleIncidenciaMaterialRepository.save(detalleExistente);
                continue;
            }

            if (material == null) {
                throw new RuntimeException("El material es obligatorio para crear el detalle");
            }

            if (material.getStock() < detalleDto.getCantidad()) {
                throw new RuntimeException("La cantidad solicitada excede el stock disponible");
            }

            material.setStock(material.getStock() - detalleDto.getCantidad());
            materialRepository.save(material);

            DetalleIncidenciaMaterial detalleNuevo = new DetalleIncidenciaMaterial();
            detalleNuevo.setIncidencia(incidencia);
            detalleNuevo.setMaterial(material);
            detalleNuevo.setCantidad_usada(detalleDto.getCantidad());
            detalleIncidenciaMaterialRepository.save(detalleNuevo);
        }
    }

    private DetalleIncidenciaMaterialDto toDto(DetalleIncidenciaMaterial detalle) {
        return new DetalleIncidenciaMaterialDto(
                detalle.getDetalle_id(),
                detalle.getMaterial() != null ? detalle.getMaterial().getMaterial_id() : null,
                detalle.getCantidad_usada()
        );
    }
    
}
