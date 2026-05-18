package com.portfolio.cryptoapi.repository;

// IMPORTS
import com.portfolio.cryptoapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Data JPA escribe el SQL automáticamente solo con leer el nombre del método.
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUsername(String username);
}