package com.portfolio.cryptoapi.exception;

//IMPORTS
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarRuntimeException(RuntimeException ex) {

        // Creamos nuestra propia respuesta JSON personalizada.
        Map<String, Object> respuestaError = new HashMap<>();

        respuestaError.put("timestamp", LocalDateTime.now());
        respuestaError.put("status", HttpStatus.BAD_REQUEST.value()); // Código 400.
        respuestaError.put("error", "Petición incorrecta");
        respuestaError.put("mensaje", ex.getMessage()); // 'Saldo insuficiente...'

        // Devolvemos el JSON con el código HTTP 400.
        return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST);
    }

    // Interceptamos los errores de argumentos inválidos.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarIllegalArgumentException(IllegalArgumentException ex) {

        Map<String, Object> respuestaError = new HashMap<>();

        respuestaError.put("timestamp", LocalDateTime.now());
        respuestaError.put("status", HttpStatus.BAD_REQUEST.value());
        respuestaError.put("error", "Argumento inválido");
        respuestaError.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST);
    }
}
