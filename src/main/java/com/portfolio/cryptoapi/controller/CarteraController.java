package com.portfolio.cryptoapi.controller;

import com.portfolio.cryptoapi.model.Cartera;
import com.portfolio.cryptoapi.service.CarteraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/carteras")
public class CarteraController {

    private final CarteraService carteraService;

    @Autowired
    public CarteraController(CarteraService carteraService) {
        this.carteraService = carteraService;
    }

    // Endpoint 1: Crear una cartera para un usuario concreto
    // Ejemplo de URL: POST http://localhost:8080/api/carteras/usuario/1
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Cartera> crearCartera(
            @PathVariable Long usuarioId, 
            @RequestBody Map<String, String> body) {
        
        // Extraemos el símbolo de la moneda del JSON que nos envían
        String simboloMoneda = body.get("simboloMoneda");
        
        Cartera nuevaCartera = carteraService.crearCartera(usuarioId, simboloMoneda);
        return new ResponseEntity<>(nuevaCartera, HttpStatus.CREATED); // Devuelve 201 Created
    }

    // Endpoint 2: Obtener todas las carteras de un usuario
    // Ejemplo de URL: GET http://localhost:8080/api/carteras/usuario/1
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Cartera>> obtenerCarterasDeUsuario(@PathVariable Long usuarioId) {
        List<Cartera> carteras = carteraService.obtenerCarterasDeUsuario(usuarioId);
        return ResponseEntity.ok(carteras); // Devuelve 200 OK
    }
}