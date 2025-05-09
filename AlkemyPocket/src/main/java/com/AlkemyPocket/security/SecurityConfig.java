package com.AlkemyPocket.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Nueva forma para deshabilitar CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/**").permitAll() // Permitir acceso libre a usuarios
                        .anyRequest().authenticated() // Todo lo demás requiere autenticación
                )
                .httpBasic(withDefaults()); // Nueva forma para habilitar HTTP Basic

        return http.build();
    }
}
