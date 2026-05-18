package com.portfolio.cryptoapi.service;

// IMPORTS
import com.portfolio.cryptoapi.model.Usuario;
import com.portfolio.cryptoapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Marca esta clase como la encargada de la lógica de negocio.
public class UsuarioService {

    // Inyectamos el repositorio para poder usar la BD.
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Método 1: Guardar un nuevo usuario en la BD.
    public Usuario registrarUsuario(Usuario nuevoUsuario) {
        // Más adelante: añadir lógica para encriptar la contraseña y verificar que el email no exista ya.
        return usuarioRepository.save(nuevoUsuario);
    }

    // Método 2: Obtener la lista de todos los usuarios.
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
}
