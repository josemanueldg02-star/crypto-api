package com.portfolio.cryptoapi.controller;

//IMPORTS
import com.portfolio.cryptoapi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credenciales) {
        try {
            // 1. Extraemos las credenciales que nos envían desde internet
            String username = credenciales.get("username");
            String password = credenciales.get("password");

            // 2. El Director comprueba si coinciden con las de nuestro usuario maestro
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // 3. ¡Son correctas! Fabricamos la tarjeta llave (JWT)
            String token = jwtUtil.generarToken(username);

            // 4. Se la entregamos al usuario
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("token", token);
            return new ResponseEntity<>(respuesta, HttpStatus.OK);

        } catch (Exception e) {
            // Si la contraseña está mal, llamamos a seguridad
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuario o contraseña incorrectos");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }
}