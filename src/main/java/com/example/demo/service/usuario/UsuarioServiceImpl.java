package com.example.demo.service.usuario;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.dtos.UsuariosDto;
import com.example.demo.enums.RolUsuario;
import com.example.demo.model.Usuario;
import com.example.demo.model.Municipalidad;
import com.example.demo.repository.MunicipalidadRepository;
import com.example.demo.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final MunicipalidadRepository municipalidadRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario createUser(UsuariosDto usuarioDto) {
        if (usuarioDto.getPassword() == null || usuarioDto.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDto.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));

        if (usuarioDto.getRol() != null && !usuarioDto.getRol().isBlank()) {
            usuario.setRol(RolUsuario.valueOf(usuarioDto.getRol().toUpperCase()));
        }

        if (usuarioDto.getMunicipalidad_id() != null) {
            Municipalidad municipalidad = municipalidadRepository.findById(usuarioDto.getMunicipalidad_id())
                    .orElseThrow(() -> new RuntimeException("Municipalidad no encontrada con id: " + usuarioDto.getMunicipalidad_id()));
            usuario.setMunicipalidad(municipalidad);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public UsuariosDto getUserById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        return new UsuariosDto(
                usuario.getUser_id(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRol() != null ? usuario.getRol().name() : null,
                usuario.getMunicipalidad() != null ? usuario.getMunicipalidad().getMunicipalidad_id() : null
        );
    }

    @Override
    public Usuario updateUser(Long id, UsuariosDto usuarioDto) {
        return usuarioRepository.findById(id)
        .map(existingUsuario -> {
            if (usuarioDto.getUsername() != null && !usuarioDto.getUsername().isBlank()) {
                existingUsuario.setUsername(usuarioDto.getUsername());
            }

            if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isBlank()) {
                existingUsuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
            }

            if (usuarioDto.getRol() != null && !usuarioDto.getRol().isBlank()) {
                existingUsuario.setRol(RolUsuario.valueOf(usuarioDto.getRol().toUpperCase()));
            }

            if (usuarioDto.getMunicipalidad_id() != null) {
                Municipalidad municipalidad = municipalidadRepository.findById(usuarioDto.getMunicipalidad_id())
                        .orElseThrow(() -> new RuntimeException("Municipalidad no encontrada con id: " + usuarioDto.getMunicipalidad_id()));
                existingUsuario.setMunicipalidad(municipalidad);
            }

            return usuarioRepository.save(existingUsuario);
        })
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<UsuariosDto> getAllUsers() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> new UsuariosDto(
                        usuario.getUser_id(),
                        usuario.getUsername(),
                        usuario.getPassword(),
                usuario.getRol() != null ? usuario.getRol().name() : null,
                usuario.getMunicipalidad() != null ? usuario.getMunicipalidad().getMunicipalidad_id() : null
                ))
                .collect(Collectors.toList());
    }

}
