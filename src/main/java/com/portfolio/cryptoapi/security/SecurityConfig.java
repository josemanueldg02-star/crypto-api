package com.portfolio.cryptoapi.security;

//IMPORTS
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // 1. EL MANUAL DE INSTRUCCIONES PRINCIPAL
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactivamos protección de formularios web (somos una API)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login").permitAll() // La puerta de la recepción está abierta para todos
                .anyRequest().authenticated() // Para todo lo demás, EXIGIMOS LA TARJETA LLAVE
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // El hotel no recuerda a nadie, solo confía en el Token
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Ponemos a nuestro recepcionista en la puerta

        return http.build();
    }

    // 2. CREAMOS UN USUARIO MAESTRO (Para probar el Login sin tocar la base de datos)
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
            .username("josemanuel")
            .password("{noop}admin123") // El {noop} le dice a Spring que por ahora no encriptaremos la contraseña
            .roles("ADMIN")
            .build();
        
        return new InMemoryUserDetailsManager(admin);
    }

    // 3. EL DIRECTOR DEL HOTEL (El que comprueba si la contraseña es correcta)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}