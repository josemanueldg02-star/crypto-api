package com.portfolio.cryptoapi.service;

// IMPORTS
import com.portfolio.cryptoapi.model.Cartera;
import com.portfolio.cryptoapi.model.Usuario;
import com.portfolio.cryptoapi.repository.CarteraRepository;
import com.portfolio.cryptoapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarteraService {
    
    private final CarteraRepository carteraRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CarteraService(CarteraRepository carteraRepository, UsuarioRepository usuarioRepository) {
        this.carteraRepository = carteraRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Método para crear una nueva cartera para un usuario.
    public Cartera crearCartera(Long usuarioId, String simboloMoneda) {
        // 1. Buscamos al usuario en la BD. Si no existe, lanzamos un ERROR.
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() ->
        new RuntimeException("Error: Usuario no encontrado con ID: " + usuarioId));

        // 2. Si el usuario existe, creamos la cartera y la relacionamos con él.
        Cartera nuevaCartera = new Cartera(simboloMoneda.toUpperCase(), usuario);

        // 3. Guardamos en la Base de Datos.
        return carteraRepository.save(nuevaCartera);
    }

    // Método para consultar qué carteras tiene un usuario en concreto.
    public List<Cartera> obtenerCarterasDeUsuario(Long usuarioId) {
        return carteraRepository.findByUsuarioId(usuarioId);
    }

    public Cartera modificarSaldo(Long usuarioId, String simboloMoneda, Double monto) {
        Cartera cartera = carteraRepository.findByUsuarioIdAndSimboloMoneda(usuarioId, simboloMoneda)
        .orElseThrow(() -> new RuntimeException("Cartera no encontrada para el usuario y moneda especificados"));

        Double saldoActual = cartera.getSaldo() != null ? cartera.getSaldo() : 0.0;
        cartera.setSaldo(saldoActual + monto);
        return carteraRepository.save(cartera);
    }
}
