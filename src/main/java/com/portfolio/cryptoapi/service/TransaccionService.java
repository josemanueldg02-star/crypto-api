package com.portfolio.cryptoapi.service;

import com.portfolio.cryptoapi.model.Cartera;
import com.portfolio.cryptoapi.model.Transaccion;
import com.portfolio.cryptoapi.repository.CarteraRepository;
import com.portfolio.cryptoapi.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CarteraRepository carteraRepository;

    @Autowired
    public TransaccionService(TransaccionRepository transaccionRepository, CarteraRepository carteraRepository) {
        this.transaccionRepository = transaccionRepository;
        this.carteraRepository = carteraRepository;
    }

    // @Transactional es vital en apps financieras. Asegura que si ocurre un error a mitad 
    // del método, se deshagan todos los cambios en la base de datos (Rollback). 
    // O se guarda el saldo Y el recibo, o no se guarda nada.
    @Transactional
    public Transaccion depositarFondos(Long carteraId, Double cantidad) {
        
        // 1. Validamos que la cantidad sea positiva
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a depositar debe ser mayor que cero");
        }

        // 2. Buscamos la cartera en la base de datos
        Cartera cartera = carteraRepository.findById(carteraId)
                .orElseThrow(() -> new RuntimeException("Cartera no encontrada con ID: " + carteraId));

        // 3. Sumamos el dinero al saldo de la cartera
        cartera.setSaldo(cartera.getSaldo() + cantidad);
        carteraRepository.save(cartera);

        // 4. Creamos el recibo (La Transacción) para dejar rastro en la auditoría
        Transaccion recibo = new Transaccion("DEPOSITO", cantidad, cartera.getSimboloMoneda(), cartera);
        
        // 5. Guardamos y devolvemos la transacción
        return transaccionRepository.save(recibo);
    }
}