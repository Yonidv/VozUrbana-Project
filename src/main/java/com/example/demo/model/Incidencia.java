package com.example.demo.model;

import java.util.List;

import com.example.demo.enums.EstadoIncidencia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long incidencia_id;
    public String descripcion;
    
    public String url_foto;
    public int cantidad_usada;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoIncidencia estado;

    @ManyToOne
    @JoinColumn(name = "municipalidad_id")
    private Municipalidad municipalidad;

    @OneToMany(mappedBy = "municipalidad")
    private List<DetalleIncidenciaMaterial> detalleIncidenciaMaterial;
    
}
