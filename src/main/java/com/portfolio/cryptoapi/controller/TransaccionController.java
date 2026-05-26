package com.portfolio.cryptoapi.controller;

// iMPORTS
import com.portfolio.cryptoapi.model.Transaccion;
import com.portfolio.cryptoapi.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    @Autowired
    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    // Endpoint para depositar fondos (dinero simulado) en una cartera específica
    // POST http://localhost:8080/api/transacciones/deposito/cartera/{carteraId}
    @PostMapping("/deposito/cartera/{carteraId}")
    public ResponseEntity<Transaccion> realizarDeposito(
        @PathVariable Long carteraId,
        @RequestBody Map<String, Double> body) {

            // Extraemos la cantidad del cuerpo de la petición JSON.
            Double cantidad = body.get("cantidad");

            Transaccion deposito = transaccionService.depositarFondos(carteraId, cantidad);
            return new ResponseEntity<>(deposito, HttpStatus.CREATED);
        }

    // Endpoint para comprar criptos.
    // POST http://localhost:8080/api/transacciones/comprar/usuario/{usuarioId})
    @PostMapping("/comprar/usuario/{usuarioId}")
    public ResponseEntity<List<Transaccion>> comprarCriptomoneda(
        @PathVariable Long usuarioId,
        @RequestBody Map<String, Object> body) {

            // Extraemos los datos del JSON
            String monedaOrigen = (String) body.get("monedaOrigen");
            String monedaDestino = (String) body.get("monedaDestino");

            // Convertimos a Double de forma segura por si llega como entero desde el JSON.
            Double cantidad = Double.valueOf(body.get("cantidad").toString());

            List<Transaccion> recibos = transaccionService.comprarCripto(usuarioId, monedaOrigen, monedaDestino, cantidad);

            return new ResponseEntity<>(recibos, HttpStatus.OK);
        }
}
