package com.portfolio.cryptoapi.security;

//IMPORTS
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;


@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        // 1. Miramos la cabecera de la petición buscando la autorización
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. Si trae la tarjeta llave (siempre empieza por la palabra "Bearer ")
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Recortamos los primeros 7 caracteres ("Bearer ")
            try {
                // Metemos la llave en nuestra máquina para ver a nombre de quién está
                username = jwtUtil.extraerUsuario(jwt);
            } catch (Exception e) {
                System.out.println("Token inválido o modificado por un atacante");
            }
        }

        // 3. Si hemos encontrado un usuario válido en la llave y todavía no ha pasado la barrera de seguridad...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Verificamos criptográficamente que la llave no esté caducada ni falsificada
            if (jwtUtil.validarToken(jwt, username)) {
                
                // Le damos una acreditación oficial de "Usuario Autenticado"
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Le abrimos la barrera
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 4. Pase lo que pase (tenga llave o no), dejamos que la petición siga su curso.
        filterChain.doFilter(request, response);
    }
}