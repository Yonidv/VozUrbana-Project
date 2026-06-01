package com.example.demo.service.usuario;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public Usuario createUser(UsuariosDto usuarioDto, Long municipalidadId) {
        if (usuarioDto.getPassword() == null || usuarioDto.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (municipalidadId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no tiene municipalidad asignada");
        }

        Municipalidad municipalidad = municipalidadRepository.findById(municipalidadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipalidad no encontrada con id: " + municipalidadId));

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDto.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));

        if (usuarioDto.getRol() != null && !usuarioDto.getRol().isBlank()) {
            usuario.setRol(RolUsuario.valueOf(usuarioDto.getRol().toUpperCase()));
        }

        usuario.setMunicipalidad(municipalidad);

        return usuarioRepository.save(usuario);
    }

    @Override
        public UsuariosDto getUserById(Long id, Long municipalidadId) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + id));

        validarMunicipalidad(usuario, municipalidadId);

        return new UsuariosDto(
                usuario.getUser_id(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRol() != null ? usuario.getRol().name() : null,
                usuario.getMunicipalidad() != null ? usuario.getMunicipalidad().getMunicipalidad_id() : null
        );
    }

    @Override
    public Usuario updateUser(Long id, UsuariosDto usuarioDto, Long municipalidadId) {
        return usuarioRepository.findById(id)
        .map(existingUsuario -> {
            validarMunicipalidad(existingUsuario, municipalidadId);

            if (usuarioDto.getUsername() != null && !usuarioDto.getUsername().isBlank()) {
                existingUsuario.setUsername(usuarioDto.getUsername());
            }

            if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isBlank()) {
                existingUsuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
            }

            if (usuarioDto.getRol() != null && !usuarioDto.getRol().isBlank()) {
                existingUsuario.setRol(RolUsuario.valueOf(usuarioDto.getRol().toUpperCase()));
            }

            return usuarioRepository.save(existingUsuario);
        })
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + id));
    }

    @Override
    public void deleteUser(Long id, Long municipalidadId) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + id));
        validarMunicipalidad(usuario, municipalidadId);
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<UsuariosDto> getAllUsers(Long municipalidadId) {
        if (municipalidadId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no tiene municipalidad asignada");
        }
        return usuarioRepository.findAllByMunicipalidadId(municipalidadId).stream()
                .map(usuario -> new UsuariosDto(
                        usuario.getUser_id(),
                        usuario.getUsername(),
                        usuario.getPassword(),
                usuario.getRol() != null ? usuario.getRol().name() : null,
                usuario.getMunicipalidad() != null ? usuario.getMunicipalidad().getMunicipalidad_id() : null
                ))
                .collect(Collectors.toList());
    }

    private void validarMunicipalidad(Usuario usuario, Long municipalidadId) {
        Long usuarioMunicipalidadId = usuario.getMunicipalidad() != null ? usuario.getMunicipalidad().getMunicipalidad_id() : null;
        if (municipalidadId == null || usuarioMunicipalidadId == null || !usuarioMunicipalidadId.equals(municipalidadId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a este usuario");
        }
    }

}
