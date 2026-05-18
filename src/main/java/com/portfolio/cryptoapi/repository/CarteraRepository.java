package com.portfolio.cryptoapi.repository;

// IMPORTS
import com.portfolio.cryptoapi.model.Cartera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarteraRepository extends JpaRepository<Cartera, Long> {

    /*
    Nombrando este método, SPRING crea el SQL para buscar
    todas las carteras que pertenezcan a un ID de usuario específico.
    Equivalente a: SELECT * FROM carteras WHERE usuario_id = ?
    */
   List<Cartera> findByUsuarioId(Long usuarioId);
}
