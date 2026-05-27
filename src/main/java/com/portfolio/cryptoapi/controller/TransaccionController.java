package com.portfolio.cryptoapi.controller;

//IMPORTS
import com.portfolio.cryptoapi.model.Transaccion;
import com.portfolio.cryptoapi.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    @Autowired
    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping("/deposito/cartera/{carteraId}")
    public ResponseEntity<Transaccion> realizarDeposito(@PathVariable Long carteraId, @RequestBody Map<String, Double> body) {
        Double cantidad = body.get("cantidad");
        Transaccion deposito = transaccionService.depositarFondos(carteraId, cantidad);
        return new ResponseEntity<>(deposito, HttpStatus.CREATED);
    }

    // --- ENDPOINT PARA RETIRAR DINERO AL BANCO ---
    @PostMapping("/retiro/cartera/{carteraId}")
    public ResponseEntity<Transaccion> realizarRetiro(@PathVariable Long carteraId, @RequestBody Map<String, Double> body) {
        Double cantidad = body.get("cantidad");
        Transaccion retiro = transaccionService.retirarFondos(carteraId, cantidad);
        return new ResponseEntity<>(retiro, HttpStatus.OK);
    }

    @PostMapping("/comprar/usuario/{usuarioId}")
    public ResponseEntity<List<Transaccion>> comprarCriptomoneda(@PathVariable Long usuarioId, @RequestBody Map<String, Object> body) {
        String monedaOrigen = (String) body.get("monedaOrigen");
        String monedaDestino = (String) body.get("monedaDestino");
        Double cantidad = Double.valueOf(body.get("cantidad").toString());
        
        List<Transaccion> recibos = transaccionService.comprarCripto(usuarioId, monedaOrigen, monedaDestino, cantidad);
        return new ResponseEntity<>(recibos, HttpStatus.OK); 
    }

    // --- ENDPOINT PARA VENDER CRIPTOMONEDAS ---
    @PostMapping("/vender/usuario/{usuarioId}")
    public ResponseEntity<List<Transaccion>> venderCriptomoneda(@PathVariable Long usuarioId, @RequestBody Map<String, Object> body) {
        String monedaOrigen = (String) body.get("monedaOrigen");
        String monedaDestino = (String) body.get("monedaDestino");
        Double cantidad = Double.valueOf(body.get("cantidad").toString());
        
        List<Transaccion> recibos = transaccionService.venderCripto(usuarioId, monedaOrigen, monedaDestino, cantidad);
        return new ResponseEntity<>(recibos, HttpStatus.OK); 
    }
}