package com.example.demo.model;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Municipalidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long municipalidad_id;
    public String nombre_municipio;
    
    @OneToMany(mappedBy = "municipalidad")
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "municipalidad")
    private List<Material> materiales;

    @OneToMany(mappedBy = "municipalidad")
    private List<Incidencia> incidencias;
}
