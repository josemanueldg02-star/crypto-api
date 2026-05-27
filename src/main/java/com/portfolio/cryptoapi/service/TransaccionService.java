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
    private final BinanceService binanceService; // NUEVO: Conectamos nuestro buscador de precios

    @Autowired
    public TransaccionService(TransaccionRepository transaccionRepository, CarteraRepository carteraRepository, BinanceService binanceService) {
        this.transaccionRepository = transaccionRepository;
        this.carteraRepository = carteraRepository;
        this.binanceService = binanceService; // NUEVO: Lo inicializamos
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

    @Transactional
    public List<Transaccion> comprarCripto(Long usuarioId, String monedaOrigen, String monedaDestino, Double cantidadGasto) {
        
        // 1. Localizamos ambas carteras del usuario en la base de datos
        Cartera carteraOrigen = carteraRepository.findByUsuarioIdAndSimboloMoneda(usuarioId, monedaOrigen)
                .orElseThrow(() -> new RuntimeException("Cartera origen no encontrada: " + monedaOrigen));
                
        Cartera carteraDestino = carteraRepository.findByUsuarioIdAndSimboloMoneda(usuarioId, monedaDestino)
                .orElseThrow(() -> new RuntimeException("Cartera destino no encontrada: " + monedaDestino));

        // 2. Auditoría de fondos: ¿Tiene dinero suficiente?
        if (carteraOrigen.getSaldo() < cantidadGasto) {
            throw new RuntimeException("Saldo insuficiente en la cartera de " + monedaOrigen);
        }

        // 3. El Motor de Cambio: ¡CONECTADO AL MUNDO REAL!
        // Le pedimos a Binance el precio actual pasando el par (Ej: BTC y EUR)
        Double precioRealMercado = binanceService.obtenerPrecioEnTiempoReal(monedaDestino, monedaOrigen);
        Double cantidadObtenida = cantidadGasto / precioRealMercado;

        // 4. Ejecutamos el movimiento de capital
        carteraOrigen.setSaldo(carteraOrigen.getSaldo() - cantidadGasto);
        carteraDestino.setSaldo(carteraDestino.getSaldo() + cantidadObtenida);

        carteraRepository.save(carteraOrigen);
        carteraRepository.save(carteraDestino);

        // 5. Generamos la doble auditoría (El rastro del dinero)
        Transaccion reciboSalida = new Transaccion("COMPRA_SALIDA", -cantidadGasto, monedaOrigen, carteraOrigen);
        Transaccion reciboEntrada = new Transaccion("COMPRA_ENTRADA", cantidadObtenida, monedaDestino, carteraDestino);

        transaccionRepository.save(reciboSalida);
        transaccionRepository.save(reciboEntrada);

        // Devolvemos ambos recibos al usuario
        return Arrays.asList(reciboSalida, reciboEntrada);
    }
}