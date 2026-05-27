package com.portfolio.cryptoapi.service;

//IMPORTS
import com.portfolio.cryptoapi.model.Cartera;
import com.portfolio.cryptoapi.model.Transaccion;
import com.portfolio.cryptoapi.repository.CarteraRepository;
import com.portfolio.cryptoapi.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CarteraRepository carteraRepository;
    private final BinanceService binanceService;

    @Autowired
    public TransaccionService(TransaccionRepository transaccionRepository, CarteraRepository carteraRepository, BinanceService binanceService) {
        this.transaccionRepository = transaccionRepository;
        this.carteraRepository = carteraRepository;
        this.binanceService = binanceService;
    }

    @Transactional
    public Transaccion depositarFondos(Long carteraId, Double cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a depositar debe ser mayor que cero");
        }
        Cartera cartera = carteraRepository.findById(carteraId)
                .orElseThrow(() -> new RuntimeException("Cartera no encontrada con ID: " + carteraId));

        cartera.setSaldo(cartera.getSaldo() + cantidad);
        carteraRepository.save(cartera);

        Transaccion recibo = new Transaccion("DEPOSITO", cantidad, cartera.getSimboloMoneda(), cartera);
        return transaccionRepository.save(recibo);
    }

    // --- MOTOR DE RETIROS ---
    @Transactional
    public Transaccion retirarFondos(Long carteraId, Double cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a retirar debe ser mayor que cero");
        }
        Cartera cartera = carteraRepository.findById(carteraId)
                .orElseThrow(() -> new RuntimeException("Cartera no encontrada con ID: " + carteraId));
        
        // Validación extra: no podemos dejar sacar dinero que no existe
        if (cartera.getSaldo() < cantidad) {
            throw new RuntimeException("Saldo insuficiente para realizar el retiro");
        }

        cartera.setSaldo(cartera.getSaldo() - cantidad);
        carteraRepository.save(cartera);

        Transaccion recibo = new Transaccion("RETIRO", -cantidad, cartera.getSimboloMoneda(), cartera);
        return transaccionRepository.save(recibo);
    }

    @Transactional
    public List<Transaccion> comprarCripto(Long usuarioId, String monedaOrigen, String monedaDestino, Double cantidadGasto) {
        Cartera carteraOrigen = carteraRepository.findByUsuarioIdAndSimboloMoneda(usuarioId, monedaOrigen)
                .orElseThrow(() -> new RuntimeException("Cartera origen no encontrada: " + monedaOrigen));
        Cartera carteraDestino = carteraRepository.findByUsuarioIdAndSimboloMoneda(usuarioId, monedaDestino)
                .orElseThrow(() -> new RuntimeException("Cartera destino no encontrada: " + monedaDestino));

        if (carteraOrigen.getSaldo() < cantidadGasto) {
            throw new RuntimeException("Saldo insuficiente en la cartera de " + monedaOrigen);
        }

        Double precioRealMercado = binanceService.obtenerPrecioEnTiempoReal(monedaDestino, monedaOrigen);
        Double cantidadObtenida = cantidadGasto / precioRealMercado;

        carteraOrigen.setSaldo(carteraOrigen.getSaldo() - cantidadGasto);
        carteraDestino.setSaldo(carteraDestino.getSaldo() + cantidadObtenida);

        carteraRepository.save(carteraOrigen);
        carteraRepository.save(carteraDestino);

        Transaccion reciboSalida = new Transaccion("COMPRA_SALIDA", -cantidadGasto, monedaOrigen, carteraOrigen);
        Transaccion reciboEntrada = new Transaccion("COMPRA_ENTRADA", cantidadObtenida, monedaDestino, carteraDestino);

        transaccionRepository.save(reciboSalida);
        transaccionRepository.save(reciboEntrada);

        return Arrays.asList(reciboSalida, reciboEntrada);
    }

    // --- MOTOR DE VENTAS ---
    @Transactional
    public List<Transaccion> venderCripto(Long usuarioId, String monedaOrigen, String monedaDestino, Double cantidadCriptoVender) {
        Cartera carteraOrigen = carteraRepository.findByUsuarioIdAndSimboloMoneda(usuarioId, monedaOrigen)
                .orElseThrow(() -> new RuntimeException("Cartera origen no encontrada: " + monedaOrigen));
        Cartera carteraDestino = carteraRepository.findByUsuarioIdAndSimboloMoneda(usuarioId, monedaDestino)
                .orElseThrow(() -> new RuntimeException("Cartera destino no encontrada: " + monedaDestino));
        
        if (carteraOrigen.getSaldo() < cantidadCriptoVender) {
            throw new RuntimeException("Saldo insuficiente en la cartera de " + monedaOrigen);
        }

        // Al vender, el origen es BTC y el destino es EUR. Binance necesita el par BTCEUR.
        Double precioRealMercado = binanceService.obtenerPrecioEnTiempoReal(monedaOrigen, monedaDestino);
        
        // ¡La matemática cambia! Si vendo 1 BTC y el precio es 60.000, obtengo 60.000 EUR (Multiplicación)
        Double cantidadObtenidaFiat = cantidadCriptoVender * precioRealMercado;

        carteraOrigen.setSaldo(carteraOrigen.getSaldo() - cantidadCriptoVender);
        carteraDestino.setSaldo(carteraDestino.getSaldo() + cantidadObtenidaFiat);

        carteraRepository.save(carteraOrigen);
        carteraRepository.save(carteraDestino);

        Transaccion reciboSalida = new Transaccion("VENTA_SALIDA", -cantidadCriptoVender, monedaOrigen, carteraOrigen);
        Transaccion reciboEntrada = new Transaccion("VENTA_ENTRADA", cantidadObtenidaFiat, monedaDestino, carteraDestino);

        transaccionRepository.save(reciboSalida);
        transaccionRepository.save(reciboEntrada);

        return Arrays.asList(reciboSalida, reciboEntrada);
    }
}