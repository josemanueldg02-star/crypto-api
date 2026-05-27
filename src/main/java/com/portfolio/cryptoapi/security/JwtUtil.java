package com.portfolio.cryptoapi.security;

//IMPORTS
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Component
public class JwtUtil {

    // Generamos una clave maestra súper segura para firmar los tokens.
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tiempo de validez del token: 24 horas (en milisegundos)
    private static final long JWT_EXPIRATION = 1000 * 60 * 60 * 24;

    // 1. FABRICAR LA LLAVE (Generar Token cuando el usuario hace Login)
    public String generarToken(String username) {
        return Jwts.builder()
                .setSubject(username) // A quién le pertenece la llave
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // Fecha de caducidad
                .signWith(SECRET_KEY) // Firmamos con nuestra clave maestra
                .compact(); // Empaquetar
    }

    // 2. LEER LA LLAVE (Extraer el usuario que viene oculto dentro del token)
    public String extraerUsuario(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // 3. COMPROBAR CADUCIDAD
    private boolean isTokenExpirado(String token) {
        return extraerClaim(token, Claims::getExpiration).before(new Date());
    }

    // 4. VALIDACIÓN TOTAL (¿Es de este usuario y sigue activo?)
    public boolean validarToken(String token, String username) {
        final String tokenUsername = extraerUsuario(token);
        return (tokenUsername.equals(username) && !isTokenExpirado(token));
    }

    // Herramienta interna para desencriptar el Token usando nuestra SECRET_KEY
    private <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}