package com.example.demo.model;
import java.util.List;

import jakarta.persistence.Entity;
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
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long material_id;
    public String nombre_item;
    public int stock;

    @ManyToOne
    @JoinColumn(name = "municipalidad_id")
    private Municipalidad municipalidad;

    @OneToMany(mappedBy = "material")
    private List<DetalleIncidenciaMaterial> detalleIncidenciaMaterial;
}
