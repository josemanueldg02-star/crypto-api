package com.portfolio.cryptoapi.controller;

import com.portfolio.cryptoapi.model.Usuario;
import com.portfolio.cryptoapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController 
@RequestMapping("/api/usuarios") // La URL base para todos los endpoints de este archivo
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint 1: Crear un usuario (Petición POST a http://localhost:8080/api/usuarios)
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
        // @Valid activa las reglas del modelo (como que el email sea válido)
        // @RequestBody intercepta el JSON que nos envían por internet y lo convierte en objeto Java
        Usuario usuarioCreado = usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED); // Devuelve código 201 (Created)
    }

    // Endpoint 2: Listar todos los usuarios (Petición GET a http://localhost:8080/api/usuarios)
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios); // Devuelve código 200 (OK) junto con la lista
    }
}