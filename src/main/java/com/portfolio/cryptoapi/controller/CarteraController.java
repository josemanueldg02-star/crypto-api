package com.portfolio.cryptoapi.controller;

import com.portfolio.cryptoapi.model.Cartera;
import com.portfolio.cryptoapi.service.CarteraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/carteras")
public class CarteraController {

    private final CarteraService carteraService;

    @Autowired
    public CarteraController(CarteraService carteraService) {
        this.carteraService = carteraService;
    }

    public static class NuevaCarteraRequest {
        private String simboloMoneda;

        public String getSimboloMoneda() {
            return simboloMoneda;
        }

        public void setSimboloMoneda(String simboloMoneda) {
            this.simboloMoneda = simboloMoneda;
        }
    }

   
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Cartera> crearCartera(
            @PathVariable("usuarioId") Long usuarioId, 
            @RequestParam("simboloMoneda") String simboloMoneda) { 
        
        Cartera nuevaCartera = carteraService.crearCartera(usuarioId, simboloMoneda);
        return new ResponseEntity<>(nuevaCartera, HttpStatus.CREATED);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Cartera>> obtenerCarterasDeUsuario(@PathVariable("usuarioId") Long usuarioId) {
        List<Cartera> carteras = carteraService.obtenerCarterasDeUsuario(usuarioId);
        return ResponseEntity.ok(carteras);
    }

    // Modificar el saldo de una cryptomoneda existente en cartera.
    @PutMapping("/usuario/{usuarioId}/modificar")
    public ResponseEntity<Cartera> modificarSaldo(
        @PathVariable("usuarioId") Long usuarioId,
        @RequestParam("simboloMoneda") String simboloMoneda,
        @RequestParam("monto") Double monto) {
            
            Cartera carteraActualizada = carteraService.modificarSaldo(usuarioId, simboloMoneda, monto);
            return ResponseEntity.ok(carteraActualizada);
        }

}