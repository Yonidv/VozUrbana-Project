package com.example.demo.service.usuario;

import java.util.List;

import com.example.demo.model.Usuario;

public interface UsuarioService {
    Usuario createUsuario(Usuario usuario);
    Usuario getUsuarioById(Long id);
    Usuario updateUsuario(Long id, Usuario usuario);
    void deleteUsuario(Long id);
    List<Usuario> getAllUsuarios();

}
